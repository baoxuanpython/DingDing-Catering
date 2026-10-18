package com.dingdingcatering.service.admin.impl;


import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.dto.OrdersCancelDTO;
import com.dingdingcatering.dto.OrdersConfirmDTO;
import com.dingdingcatering.dto.OrdersPageQueryDTO;
import com.dingdingcatering.dto.OrdersRejectionDTO;
import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.Orders;
import com.dingdingcatering.exception.OrderBusinessException;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.mapper.admin.OrderMapper;
import com.dingdingcatering.mapper.admin.SetMealMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.OrderService;
import com.dingdingcatering.util.OrderDetailEnrichUtil;
import com.dingdingcatering.vo.OrderStatisticsVO;
import com.dingdingcatering.vo.OrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("adminOrderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final DishMapper dishMapper;
    private final SetMealMapper setMealMapper;

    public OrderServiceImpl(OrderMapper orderMapper, DishMapper dishMapper, SetMealMapper setMealMapper) {
        this.orderMapper = orderMapper;
        this.dishMapper = dishMapper;
        this.setMealMapper = setMealMapper;
    }

    public static void enrichOrderVOListFromAdmin(List<OrderVO> orderVOList, List<OrderDetail> orderDetailList, Function<OrderVO, Long> orderIdGetter) {
        Map<Long, List<OrderDetail>> detailMap = orderDetailList.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));
        Map<Long, List<String>> dishesMap = orderDetailList.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId,
                        Collectors.mapping(OrderDetail::getName, Collectors.toList())));

        orderVOList.forEach(orderVO -> {
            Long orderId = orderIdGetter.apply(orderVO);
            orderVO.setOrderDetailList(detailMap.getOrDefault(orderId, Collections.emptyList()));
            orderVO.setOrderDishes(String.join("、", dishesMap.getOrDefault(orderId, Collections.emptyList())));
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<OrderVO> pageQueryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        if (ordersPageQueryDTO.getEndTime() != null) {
            ordersPageQueryDTO.setEndTime(ordersPageQueryDTO.getEndTime().withHour(23).withMinute(59).withSecond(59));
        }
        log.debug("分页查询订单: page={}, pageSize={}, number={}, status={}",
                ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize(),
                ordersPageQueryDTO.getNumber(), ordersPageQueryDTO.getStatus());
        try (Page<OrderVO> page = PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize())) {
            List<OrderVO> orderList = orderMapper.queryOrders(ordersPageQueryDTO);

            if (!orderList.isEmpty()) {
                // 批量获取订单ID，组成列表
                List<Long> orderIds = orderList.stream().map(OrderVO::getId).collect(Collectors.toList());
                //批量获取订单详情
                List<OrderDetail> details = orderMapper.queryOrderDetails(orderIds);
                // 填充订单明细中的菜品/套餐完整信息
                OrderDetailEnrichUtil.enrichOrderDetails(details, dishMapper, setMealMapper);
                // 使用工具类填充订单详情和菜品信息
                enrichOrderVOListFromAdmin(orderList, details, OrderVO::getId);
            }
            log.debug("分页查询订单完成: total={}", page.getTotal());
            return new PageResult<>(page.getTotal(), orderList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO queryOrderDetails(String number) {
        log.debug("查询订单详情: number={}", number);
        OrderVO orderVO = orderMapper.queryByNumber(number);
        if (orderVO == null) {
            log.warn("查询订单详情失败: 订单不存在, number={}", number);
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        List<OrderDetail> orderDetailList = orderMapper.queryOrderDetails(Collections.singletonList(orderVO.getId()));
        // 填充订单明细中的菜品/套餐完整信息
        OrderDetailEnrichUtil.enrichOrderDetails(orderDetailList, dishMapper, setMealMapper);
        orderVO.setOrderDetailList(orderDetailList);
        orderVO.setOrderDishes(orderDetailList.stream().map(OrderDetail::getName).collect(Collectors.joining("、")));
        return orderVO;
    }

    @Override
    public void cancelById(OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单: id={}, reason={}", ordersCancelDTO.getId(), ordersCancelDTO.getCancelReason());
        OrderVO orderVO = orderMapper.queryById(ordersCancelDTO.getId());
        if (orderVO == null) {
            log.warn("取消订单失败: 订单不存在, id={}", ordersCancelDTO.getId());
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (Objects.equals(orderVO.getStatus(), Orders.COMPLETED) || Objects.equals(orderVO.getStatus(), Orders.CANCELLED)) {
            log.warn("取消订单失败: 订单状态不正确, id={}, status={}", ordersCancelDTO.getId(), orderVO.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "已完成或已取消的订单不能取消");
        }
        orderMapper.cancelById(ordersCancelDTO);
        log.info("取消订单成功: id={}", ordersCancelDTO.getId());
    }

    @Override
    public OrderStatisticsVO statistics() {
        log.debug("查询订单统计");
        return orderMapper.statistics();
    }

    @Override
    public void completeById(Long id) {
        log.info("完成订单: id={}", id);
        OrderVO orderVO = orderMapper.queryById(id);
        if (orderVO == null) {
            log.warn("完成订单失败: 订单不存在, id={}", id);
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!Objects.equals(orderVO.getPayStatus(), Orders.PAID)) {
            log.warn("完成订单失败: 订单未支付, id={}, payStatus={}", id, orderVO.getPayStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "订单未支付，不能完成");
        }
        if (!Objects.equals(orderVO.getStatus(), Orders.DELIVERY_IN_PROGRESS)) {
            log.warn("完成订单失败: 订单状态不正确, id={}, status={}", id, orderVO.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有派送中的订单才能完成");
        }
        orderMapper.completeById(id);
        log.info("完成订单成功: id={}", id);
    }

    @Override
    public void rejectionById(OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒绝订单: id={}, reason={}", ordersRejectionDTO.getId(), ordersRejectionDTO.getRejectionReason());
        OrderVO orderVO = orderMapper.queryById(ordersRejectionDTO.getId());
        if (orderVO == null) {
            log.warn("拒绝订单失败: 订单不存在, id={}", ordersRejectionDTO.getId());
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!Objects.equals(orderVO.getStatus(), Orders.TO_BE_CONFIRMED)) {
            log.warn("拒绝订单失败: 订单状态不正确, id={}, status={}", ordersRejectionDTO.getId(), orderVO.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有待接单的订单才能拒绝");
        }
        orderMapper.rejectionById(ordersRejectionDTO);
        log.info("拒绝订单成功: id={}", ordersRejectionDTO.getId());
    }

    @Override
    public void confirmById(OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单: id={}", ordersConfirmDTO.getId());
        OrderVO orderVO = orderMapper.queryById(ordersConfirmDTO.getId());
        if (orderVO == null) {
            log.warn("接单失败: 订单不存在, id={}", ordersConfirmDTO.getId());
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!Objects.equals(orderVO.getStatus(), Orders.TO_BE_CONFIRMED)) {
            log.warn("接单失败: 订单状态不正确, id={}, status={}", ordersConfirmDTO.getId(), orderVO.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有待接单的订单才能接单");
        }
        ordersConfirmDTO.setStatus(Orders.CONFIRMED);
        orderMapper.confirmById(ordersConfirmDTO);
        log.info("接单成功: id={}", ordersConfirmDTO.getId());
    }

    @Override
    public void deliveryById(Long id) {
        log.info("派送订单: id={}", id);
        OrderVO orderVO = orderMapper.queryById(id);
        if (orderVO == null) {
            log.warn("派送订单失败: 订单不存在, id={}", id);
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!Objects.equals(orderVO.getStatus(), Orders.CONFIRMED)) {
            log.warn("派送订单失败: 订单状态不正确, id={}, status={}", id, orderVO.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有已接单的订单才能派送");
        }
        orderMapper.deliveryById(id);
        log.info("派送订单成功: id={}", id);
    }
}