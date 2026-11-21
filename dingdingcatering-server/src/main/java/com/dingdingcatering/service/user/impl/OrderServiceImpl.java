package com.dingdingcatering.service.user.impl;

import com.alibaba.fastjson2.JSON;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.dto.OrdersCancelDTO;
import com.dingdingcatering.dto.OrdersPaymentDTO;
import com.dingdingcatering.dto.OrdersSubmitDTO;
import com.dingdingcatering.dto.SetmealFlavorDTO;
import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.entity.Message;
import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.Orders;
import com.dingdingcatering.entity.SetMealDish;
import com.dingdingcatering.entity.ShoppingCart;
import com.dingdingcatering.exception.AddressBookBusinessException;
import com.dingdingcatering.exception.OrderBusinessException;
import com.dingdingcatering.exception.ShoppingCartBusinessException;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.mapper.admin.SetMealMapper;
import com.dingdingcatering.mapper.user.AddressBookMapper;
import com.dingdingcatering.mapper.user.OrderMapper;
import com.dingdingcatering.mapper.user.ShoppingCartMapper;
import com.dingdingcatering.mapper.user.UserMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.common.MessageService;
import com.dingdingcatering.service.user.OrderService;
import com.dingdingcatering.util.OrderDetailEnrichUtil;
import com.dingdingcatering.util.SetmealFlavorCodec;
import com.dingdingcatering.vo.OrderPaymentVO;
import com.dingdingcatering.vo.OrderSubmitVO;
import com.dingdingcatering.vo.OrderVO;
import com.dingdingcatering.websocket.WebSocketServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.dingdingcatering.service.admin.impl.OrderServiceImpl.enrichOrderVOListFromAdmin;

@Service("userOrderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final AddressBookMapper addressBookMapper;
    private final UserMapper userMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WebSocketServer webSocketServer;
    private final DishMapper dishMapper;
    private final SetMealMapper setMealMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final MessageService messageService;

    public OrderServiceImpl(OrderMapper orderMapper, AddressBookMapper addressBookMapper, UserMapper userMapper, ShoppingCartMapper shoppingCartMapper, WebSocketServer webSocketServer, DishMapper dishMapper, SetMealMapper setMealMapper, StringRedisTemplate stringRedisTemplate, MessageService messageService) {
        this.orderMapper = orderMapper;
        this.addressBookMapper = addressBookMapper;
        this.userMapper = userMapper;
        this.shoppingCartMapper = shoppingCartMapper;
        this.webSocketServer = webSocketServer;
        this.dishMapper = dishMapper;
        this.setMealMapper = setMealMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.messageService = messageService;
    }

    @Override
    public void sendReminder(Long id) {
        Long userId = BaseContext.getCurrentId();
        Orders orders = orderMapper.getOrdersById(id, userId);
        if (orders == null) {
            log.warn("催单失败，订单不存在或不属于当前用户: orderId={}, userId={}", id, userId);
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!isRemindableStatus(orders.getStatus())) {
            log.warn("催单失败，订单状态不支持催单: orderId={}, status={}", id, orders.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "当前订单状态不支持催单");
        }

        String limitKey = "order:reminder:" + userId + ":" + id;
        Boolean acquired = stringRedisTemplate.opsForValue().setIfAbsent(limitKey, "1", Duration.ofMinutes(1));
        if (!Boolean.TRUE.equals(acquired)) {
            log.warn("催单失败，60秒内重复催单: orderId={}, userId={}", id, userId);
            throw new OrderBusinessException(ErrorCode.ORDER_REMINDER_TOO_FREQUENT);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("type", Message.TYPE_REMINDER);
        map.put("orderId", id);
        map.put("orderNumber", orders.getNumber());
        map.put("content", MessageConstant.MESSAGE_REMINDER);

        try {
            messageService.save(Message.TYPE_REMINDER, id, orders.getNumber(), MessageConstant.MESSAGE_REMINDER);
        } catch (Exception e) {
            log.error("催单消息落库失败，不影响催单主流程: orderId={}", id, e);
        }
        try {
            webSocketServer.sendToAdminClient(JSON.toJSONString(map));
        } catch (Exception e) {
            log.error("催单消息推送失败，不影响催单主流程: orderId={}", id, e);
        }
        log.info("催单提醒已推送至管理端: orderId={}, orderNumber={}, userId={}", id, orders.getNumber(), userId);
    }

    /**
     * 判断订单状态是否允许催单
     * <p>
     * 待接单、已接单、派送中三个状态允许催单；
     * 待支付、已完成、已取消、已退款不支持催单
     */
    private boolean isRemindableStatus(Integer status) {
        return Orders.TO_BE_CONFIRMED.equals(status)
                || Orders.CONFIRMED.equals(status)
                || Orders.DELIVERY_IN_PROGRESS.equals(status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("开始提交订单: userId={}, addressBookId={}", userId, ordersSubmitDTO.getAddressBookId());
        AddressBook addressBook = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId(), userId);
        if (addressBook == null) {
            log.error("地址不存在: addressBookId={}, userId={}", ordersSubmitDTO.getAddressBookId(), userId);
            throw new AddressBookBusinessException(ErrorCode.ADDRESS_BOOK_IS_NULL);
        }
        String userName = userMapper.getUserName(userId);

        List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByUserId(userId);
        if (shoppingCarts.isEmpty()) {
            log.error("购物车为空，无法提交订单: userId={}", userId);
            throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_IS_NULL);
        }
        log.info("购物车商品数量: {}", shoppingCarts.size());

        Orders orders = buildOrder(ordersSubmitDTO, addressBook, userName, userId);
        orderMapper.insertOrder(orders);
        log.info("订单创建成功: 订单ID={}, 订单编号={}", orders.getId(), orders.getNumber());

        List<OrderDetail> orderDetailList = buildOrderDetailList(shoppingCarts, orders.getId());
        orderMapper.insertOrderDetail(orderDetailList);
        log.info("订单明细插入成功: 明细数量={}", orderDetailList.size());

        shoppingCartMapper.cleanByUserId(userId);
        log.info("购物车已清空: userId={}", userId);
        log.info("订单提交完成: orderNumber={}, 总金额={}", orders.getNumber(), orders.getAmount());
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();
    }

    private Orders buildOrder(OrdersSubmitDTO dto, AddressBook addressBook, String userName, Long userId) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(dto, orders);
        String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomNum = String.valueOf((int) (Math.random() * 10000) + 10000).substring(1);
        orders.setNumber("DD" + dateTimeStr + randomNum);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        orders.setUserName(userName);
        orders.setConsignee(addressBook.getConsignee());
        return orders;
    }

    private OrderDetail buildOrderDetail(ShoppingCart cart, Long orderId) {
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(orderId);
        detail.setName(cart.getName());
        detail.setImage(cart.getImage());
        detail.setDishId(cart.getDishId());
        detail.setSetmealId(cart.getSetmealId());
        detail.setFlavorId(cart.getFlavorId());
        detail.setFlavorVal(cart.getFlavorVal());
        detail.setDishCopies(cart.getNumber());
        detail.setAmount(cart.getAmount());
        return detail;
    }

    /**
     * 将购物车转换为订单明细
     * <p>
     * 菜品直接 1:1 转换；套餐需要按套餐内菜品展开为多条明细，
     * 每条明细记录「套餐名称 + 套餐内单个菜品 + 该菜品选中的口味 + 份数」，
     * setmeal_copies 记录套餐份数，dish_copies 记录展开后该菜品的总份数
     */
    private List<OrderDetail> buildOrderDetailList(List<ShoppingCart> shoppingCarts, Long orderId) {
        List<Long> setmealIds = shoppingCarts.stream()
                .map(ShoppingCart::getSetmealId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, List<SetMealDish>> setmealDishMap = new HashMap<>();
        if (!setmealIds.isEmpty()) {
            setMealMapper.queryDishesBySetmealIds(setmealIds)
                    .forEach(setmealDish -> setmealDishMap
                            .computeIfAbsent(setmealDish.getSetmealId(), key -> new ArrayList<>())
                            .add(setmealDish));
        }

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getSetmealId() == null) {
                orderDetailList.add(buildOrderDetail(cart, orderId));
            } else {
                orderDetailList.addAll(buildSetmealOrderDetails(cart, orderId, setmealDishMap.get(cart.getSetmealId())));
            }
        }
        return orderDetailList;
    }

    /**
     * 套餐购物车行展开为订单明细：套餐内每道菜一条明细
     */
    private List<OrderDetail> buildSetmealOrderDetails(ShoppingCart cart, Long orderId, List<SetMealDish> setmealDishes) {
        if (CollectionUtils.isEmpty(setmealDishes)) {
            throw new OrderBusinessException(ErrorCode.ORDER_BUSINESS_ERROR, "套餐内菜品数据异常，无法下单");
        }

        Map<Long, List<SetmealFlavorCodec.FlavorPair>> flavors =
                SetmealFlavorCodec.decode(cart.getFlavorId(), cart.getFlavorVal());
        int setmealCopies = cart.getNumber() == null ? 1 : cart.getNumber();

        List<OrderDetail> details = new ArrayList<>(setmealDishes.size());
        for (SetMealDish setmealDish : setmealDishes) {
            List<SetmealFlavorCodec.FlavorPair> pairs =
                    flavors.getOrDefault(setmealDish.getDishId(), Collections.emptyList());

            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            detail.setName(cart.getName());
            detail.setImage(cart.getImage());
            detail.setSetmealId(cart.getSetmealId());
            detail.setDishId(setmealDish.getDishId());
            detail.setDishCopies((setmealDish.getCopies() == null ? 1 : setmealDish.getCopies()) * setmealCopies);
            detail.setSetmealCopies(setmealCopies);
            detail.setAmount(cart.getAmount());
            detail.setFlavorId(pairs.isEmpty() ? null :
                    pairs.stream().map(pair -> String.valueOf(pair.getFlavorId())).collect(Collectors.joining(",")));
            detail.setFlavorVal(pairs.isEmpty() ? null :
                    pairs.stream().map(SetmealFlavorCodec.FlavorPair::getFlavorVal).collect(Collectors.joining(",")));
            details.add(detail);
        }
        return details;
    }

    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        log.info("开始支付订单: orderNumber={}, payMethod={}", ordersPaymentDTO.getOrderNumber(), ordersPaymentDTO.getPayMethod());
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        orderMapper.paySuccess(ordersPaymentDTO);

        Long orderId = orderMapper.getIdByNumber(ordersPaymentDTO.getOrderNumber());
        if (orderId == null) {
            log.warn("支付成功但未查到订单主键，跳过新订单消息落库与推送: orderNumber={}", ordersPaymentDTO.getOrderNumber());
            return OrderPaymentVO.builder().estimatedDeliveryTime(time).build();
        }
        try {
            messageService.saveIfAbsent(Message.TYPE_NEW_ORDER, orderId,
                    ordersPaymentDTO.getOrderNumber(), MessageConstant.MESSAGE_NEW_ORDER);
        } catch (Exception e) {
            log.error("新订单消息落库失败，不影响支付主流程: orderNumber={}", ordersPaymentDTO.getOrderNumber(), e);
        }

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("type", Message.TYPE_NEW_ORDER);
        messageMap.put("orderId", orderId);
        messageMap.put("orderNumber", ordersPaymentDTO.getOrderNumber());
        messageMap.put("content", MessageConstant.MESSAGE_NEW_ORDER);
        try {
            webSocketServer.sendToAdminClient(JSON.toJSONString(messageMap));
        } catch (Exception e) {
            log.error("新订单消息推送失败，不影响支付主流程: orderNumber={}", ordersPaymentDTO.getOrderNumber(), e);
        }
        log.info("订单支付成功: orderNumber={}, 支付时间={}", ordersPaymentDTO.getOrderNumber(), time);
        return OrderPaymentVO.builder().estimatedDeliveryTime(time).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<OrderVO> getHistoryOrders(Integer page, Integer pageSize, Integer status) {
        Long userId = BaseContext.getCurrentId();
        log.info("查询历史订单: userId={}, page={}, pageSize={}, status={}", userId, page, pageSize, status);
        try (Page<OrderVO> pageQuery = PageHelper.startPage(page, pageSize)) {
            List<OrderVO> orderVOList = orderMapper.getHistoryOrders(userId, status);
            if (orderVOList.isEmpty()) {
                log.info("用户无订单: userId={}", userId);
                return new PageResult<>(0L, orderVOList);
            }
            enrichOrderVOList(orderVOList);
            log.info("查询历史订单成功: userId={}, 订单数={}", userId, orderVOList.size());
            return new PageResult<>(pageQuery.getTotal(), orderVOList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO getOrderDetail(Long id) {
        Long userId = BaseContext.getCurrentId();
        log.info("查询订单详情: orderId={}, userId={}", id, userId);
        Orders orders = orderMapper.getOrdersById(id, userId);
        if (orders == null) {
            log.warn("订单不存在: orderId={}, userId={}", id, userId);
            return null;
        }
        OrderVO orderVO = new OrderVO();
        org.springframework.beans.BeanUtils.copyProperties(orders, orderVO);
        enrichOrderVOList(List.of(orderVO));
        log.info("查询订单详情成功: orderId={}", id);
        return orderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repetition(Long id) {
        Long userId = BaseContext.getCurrentId();

        List<OrderDetail> orderDetailList = orderMapper.getDetailList(List.of(id));
        List<ShoppingCart> cartList = buildRepetitionCarts(orderDetailList, userId);
        cartList.forEach(shoppingCartMapper::insert);
    }

    /**
     * 根据订单明细重建购物车
     * <p>
     * 菜品明细直接回填为一条购物车行；套餐明细按「套餐ID + 归一化口味串」重新合并，
     * 避免展开后的多条明细被还原成多条「既有 dishId 又有 setmealId」的畸形购物车行
     */
    private List<ShoppingCart> buildRepetitionCarts(List<OrderDetail> orderDetailList, Long userId) {
        List<ShoppingCart> cartList = new ArrayList<>();

        for (OrderDetail detail : orderDetailList) {
            if (detail.getSetmealId() == null) {
                cartList.add(buildCartFromDetail(detail, detail.getDishCopies(), userId,
                        detail.getFlavorId(), detail.getFlavorVal()));
            }
        }

        Map<Long, List<OrderDetail>> setmealDetailGroups = orderDetailList.stream()
                .filter(detail -> detail.getSetmealId() != null)
                .collect(Collectors.groupingBy(OrderDetail::getSetmealId, LinkedHashMap::new, Collectors.toList()));

        setmealDetailGroups.values().forEach(details -> cartList.addAll(buildSetmealCartsFromDetails(details, userId)));
        return cartList;
    }

    /**
     * 把同一套餐的展开明细还原成购物车行
     * <p>
     * 展开后套餐内每道菜在每个套餐份数中各有一行，按菜品分组后第 i 行即第 i 份套餐；
     * 口味完全相同的份数合并成一行并累加 number
     */
    private List<ShoppingCart> buildSetmealCartsFromDetails(List<OrderDetail> details, Long userId) {
        Map<Long, List<OrderDetail>> detailsByDish = details.stream()
                .collect(Collectors.groupingBy(OrderDetail::getDishId, LinkedHashMap::new, Collectors.toList()));
        int copies = detailsByDish.values().stream().mapToInt(List::size).max().orElse(1);

        Map<String, ShoppingCart> mergedCarts = new LinkedHashMap<>();
        for (int index = 0; index < copies; index++) {
            OrderDetail firstDetail = null;
            List<SetmealFlavorDTO> setmealFlavors = new ArrayList<>();
            for (List<OrderDetail> dishDetails : detailsByDish.values()) {
                if (index >= dishDetails.size()) {
                    continue;
                }
                OrderDetail detail = dishDetails.get(index);
                if (detail.getDishId() == null) {
                    continue;
                }
                if (firstDetail == null) {
                    firstDetail = detail;
                }
                SetmealFlavorDTO item = new SetmealFlavorDTO();
                item.setDishId(detail.getDishId());
                item.setFlavorId(parseFlavorIds(detail.getFlavorId()));
                item.setFlavorVal(parseFlavorVals(detail.getFlavorVal()));
                if (!CollectionUtils.isEmpty(item.getFlavorId())) {
                    setmealFlavors.add(item);
                }
            }
            if (firstDetail == null) {
                continue;
            }

            SetmealFlavorCodec.EncodedFlavors encodedFlavors = SetmealFlavorCodec.encode(setmealFlavors);
            String key = firstDetail.getSetmealId() + "|" + encodedFlavors.getFlavorId() + "|" + encodedFlavors.getFlavorVal();
            ShoppingCart cart = mergedCarts.get(key);
            if (cart == null) {
                mergedCarts.put(key, buildCartFromDetail(firstDetail, 1, userId,
                        encodedFlavors.getFlavorId(), encodedFlavors.getFlavorVal()));
            } else {
                cart.setNumber(cart.getNumber() + 1);
            }
        }
        return new ArrayList<>(mergedCarts.values());
    }

    private ShoppingCart buildCartFromDetail(OrderDetail detail, Integer number, Long userId, String flavorId, String flavorVal) {
        ShoppingCart cart = new ShoppingCart();
        cart.setName(detail.getName());
        cart.setImage(detail.getImage());
        cart.setDishId(detail.getDishId());
        cart.setSetmealId(detail.getSetmealId());
        cart.setFlavorId(flavorId);
        cart.setFlavorVal(flavorVal);
        cart.setNumber(number);
        cart.setAmount(detail.getAmount());
        cart.setUserId(userId);
        cart.setCreateTime(LocalDateTime.now());
        return cart;
    }

    private List<Long> parseFlavorIds(String flavorId) {
        if (flavorId == null || flavorId.isBlank()) {
            return Collections.emptyList();
        }
        List<Long> flavorIds = new ArrayList<>();
        for (String value : flavorId.split(",")) {
            String trimmed = value.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                flavorIds.add(Long.valueOf(trimmed));
            } catch (NumberFormatException e) {
                log.warn("订单明细中的口味ID非法，已跳过: flavorId={}", flavorId);
            }
        }
        return flavorIds;
    }

    private List<String> parseFlavorVals(String flavorVal) {
        if (flavorVal == null || flavorVal.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(flavorVal.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id, OrdersCancelDTO ordersCancelDTO) {
        Long userId = BaseContext.getCurrentId();
        Orders orders = orderMapper.getOrdersById(id, userId);
        if (orders == null) {
            log.warn("订单不存在: orderId={}, userId={}", id, userId);
            return;
        }
        if (orders.getStatus() > Orders.TO_BE_CONFIRMED) {
            log.warn("订单已接单，不允许取消: orderId={}, status={}", id, orders.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "订单已被接单，无法取消");
        }
        if (Orders.PAID.equals(orders.getPayStatus())) {
            orderMapper.cancelOrderWithRefund(id, userId, ordersCancelDTO.getCancelReason());
            log.info("订单取消成功（已支付，退款处理）: orderId={}, userId={}", id, userId);
        } else {
            orderMapper.cancelOrderWithoutRefund(id, userId, ordersCancelDTO.getCancelReason());
            log.info("订单取消成功（未支付）: orderId={}, userId={}", id, userId);
        }
    }

    private void enrichOrderVOList(List<OrderVO> orderVOList) {
        List<Long> orderIds = orderVOList.stream().map(OrderVO::getId).toList();
        List<OrderDetail> orderDetailList = orderMapper.getDetailList(orderIds);
        log.debug("查询订单明细: 订单数={}, 明细数={}", orderIds.size(), orderDetailList.size());
        // 填充订单明细中的菜品/套餐完整信息
        OrderDetailEnrichUtil.enrichOrderDetails(orderDetailList, dishMapper, setMealMapper);
        enrichOrderVOListFromAdmin(orderVOList, orderDetailList, OrderVO::getId);
    }

}
