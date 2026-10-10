package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.dto.OrdersPaymentDTO;
import com.dingdingcatering.dto.OrdersSubmitDTO;
import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.Orders;
import com.dingdingcatering.entity.ShoppingCart;
import com.dingdingcatering.mapper.user.AddressBookMapper;
import com.dingdingcatering.mapper.user.OrderMapper;
import com.dingdingcatering.mapper.user.ShoppingCartMapper;
import com.dingdingcatering.mapper.user.UserMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.user.OrderService;
import com.dingdingcatering.vo.OrderSubmitVO;
import com.dingdingcatering.vo.OrderVO;
import com.dingdingcatering.vo.PaymentSuccessVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.dingdingcatering.service.admin.impl.OrderServiceImpl.enrichOrderVOListFromAdmin;

@Service("userOrderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final AddressBookMapper addressBookMapper;
    private final UserMapper userMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    public OrderServiceImpl(OrderMapper orderMapper, AddressBookMapper addressBookMapper, UserMapper userMapper, ShoppingCartMapper shoppingCartMapper) {
        this.orderMapper = orderMapper;
        this.addressBookMapper = addressBookMapper;
        this.userMapper = userMapper;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    @Override
    public void sendReminder(Long id) {
        log.info("发送订单催单提醒: orderId={}", id);
        // TODO 使用WebSocket发送提醒
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("开始提交订单: userId={}, addressBookId={}", userId, ordersSubmitDTO.getAddressBookId());

        AddressBook addressBook = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId(), userId);
        if (addressBook == null) {
            log.error("地址不存在: addressBookId={}, userId={}", ordersSubmitDTO.getAddressBookId(), userId);
            throw new RuntimeException("地址不存在");
        }
        String userName = userMapper.getUserName(userId);

        Orders orders = buildOrder(ordersSubmitDTO, addressBook, userName, userId);
        orderMapper.insertOrder(orders);
        log.info("订单创建成功: 订单ID={}, 订单编号={}", orders.getId(), orders.getNumber());

        List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByUserId(userId);
        if (shoppingCarts.isEmpty()) {
            log.error("购物车为空，无法提交订单: userId={}", userId);
            throw new RuntimeException("购物车为空");
        }
        log.info("购物车商品数量: {}", shoppingCarts.size());

        List<OrderDetail> orderDetailList = shoppingCarts.stream()
                .map(cart -> buildOrderDetail(cart, Long.parseLong(orders.getNumber())))
                .toList();
        orderMapper.insertOrderDetail(orderDetailList);
        log.info("订单明细插入成功: 明细数量={}", orderDetailList.size());

        shoppingCartMapper.cleanByUserId(userId);
        log.info("购物车已清空: userId={}", userId);
        log.info("订单提交完成: orderNumber={}, 总金额={}", orders.getNumber(), orders.getAmount());
        return OrderSubmitVO.builder()
                .id(Long.parseLong(orders.getNumber()))
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();
    }

    private Orders buildOrder(OrdersSubmitDTO dto, AddressBook addressBook, String userName, Long userId) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(dto, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()) + userId);
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
        BeanUtils.copyProperties(cart, detail);
        detail.setOrderId(orderId);
        return detail;
    }

    @Override
    public PaymentSuccessVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        log.info("开始支付订单: orderNumber={}, payMethod={}", ordersPaymentDTO.getOrderNumber(), ordersPaymentDTO.getPayMethod());
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // TODO 此处忽略了调用微信支付接口，实际支付时需要调用微信支付接口，返回支付结果
        orderMapper.paySuccess(ordersPaymentDTO);
        log.info("订单支付成功: orderNumber={}, 支付时间={}", ordersPaymentDTO.getOrderNumber(), time);
        return PaymentSuccessVO.builder().estimatedDeliveryTime(time).build();
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
        List<OrderVO> orderVOList = orderMapper.getHistoryOrders(userId, null);
        if (orderVOList.isEmpty()) {
            log.warn("订单不存在: orderId={}, userId={}", id, userId);
            return null;
        }
        OrderVO targetOrder = orderVOList.stream()
                .filter(orderVO -> Objects.equals(orderVO.getId(), id))
                .findFirst()
                .orElse(null);
        if (targetOrder == null) {
            log.warn("订单不属于当前用户: orderId={}, userId={}", id, userId);
            return null;
        }
        enrichOrderVOList(List.of(targetOrder));
        log.info("查询订单详情成功: orderId={}, orderNumber={}", id, targetOrder.getNumber());
        return targetOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repetition(Long id) {
        Long userId = BaseContext.getCurrentId();
        Long orderId = Long.parseLong(orderMapper.getOrdersById(id, userId).getNumber());
        List<OrderDetail> orderDetailList = orderMapper.getDetailList(List.of(orderId));
        List<ShoppingCart> cartList = orderDetailList.stream().map(orderDetail -> {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, cart);
            cart.setUserId(userId);
            cart.setCreateTime(LocalDateTime.now());
            return cart;
        }).toList();
        cartList.forEach(shoppingCartMapper::insert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        Long userId = BaseContext.getCurrentId();
        orderMapper.cancelOrder(id, userId);
        log.info("订单取消成功: orderId={}, userId={}，已退款", id, userId);
    }

    private void enrichOrderVOList(List<OrderVO> orderVOList) {
        List<Long> orderIds = orderVOList.stream()
                .map(orderVO -> Long.parseLong(orderVO.getNumber()))
                .toList();
        List<OrderDetail> orderDetailList = orderMapper.getDetailList(orderIds);
        log.debug("查询订单明细: 订单数={}, 明细数={}", orderIds.size(), orderDetailList.size());

        enrichOrderVOListFromAdmin(orderVOList, orderDetailList,
                orderVO -> Long.parseLong(orderVO.getNumber()));
    }
}
