package com.sky.service.admin.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.admin.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.admin.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    /**
     * 所有有效的订单状态集合
     * <p>使用 Set.of() 创建不可变集合，查找效率 O(1)</p>
     */
    private static final Set<Integer> VALID_ORDER_STATUSES = Set.of(
            Orders.PENDING_PAYMENT,      // 1 - 待付款
            Orders.TO_BE_CONFIRMED,      // 2 - 待接单
            Orders.CONFIRMED,            // 3 - 已接单
            Orders.DELIVERY_IN_PROGRESS, // 4 - 派送中
            Orders.COMPLETED,            // 5 - 已完成
            Orders.CANCELLED             // 6 - 已取消
    );

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<OrderVO> pageQueryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        try (Page<OrderVO> page = PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize())) {
            List<OrderVO> orderList = orderMapper.queryOrders(ordersPageQueryDTO);

            if (!orderList.isEmpty()) {
                // 批量获取订单ID，组成列表
                List<Long> orderIds = orderList.stream().map(OrderVO::getId).collect(Collectors.toList());
                //批量获取订单详情
                List<OrderDetail> details = orderMapper.queryOrderDetails(orderIds);
                // 按订单分组，组成详情Map（包含完整订单详情对象）
                Map<Long, List<OrderDetail>> detailMap = details.stream().collect(Collectors.groupingBy(OrderDetail::getOrderId));

                // 按订单分组，提取菜品列表（只保留name）
                Map<Long, List<String>> orderDishesMap = details.stream()
                        .collect(Collectors.groupingBy(OrderDetail::getOrderId, Collectors.mapping(OrderDetail::getName, Collectors.toList())));

                // 分别将每个订单的详情列表和菜品ID列表设置到订单VO中
                orderList.forEach(order -> {
                    order.setOrderDetailList(detailMap.getOrDefault(order.getId(), Collections.emptyList()));
                    order.setOrderDishes(String.join("、", orderDishesMap.getOrDefault(order.getId(), Collections.emptyList())));
                });
            }

            return new PageResult<>(page.getTotal(), orderList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO queryOrderDetails(Long id) {
        OrderVO orderVO = orderMapper.queryById(id);
        if (orderVO == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (!VALID_ORDER_STATUSES.contains(orderVO.getStatus())) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        List<OrderDetail> orderDetailList = orderMapper.queryOrderDetails(Collections.singletonList(id));
        orderVO.setOrderDetailList(orderDetailList);
        orderVO.setOrderDishes(orderDetailList.stream().map(OrderDetail::getName).collect(Collectors.joining("、")));
        return orderVO;
    }

    @Override
    public void cancelById(OrdersCancelDTO ordersCancelDTO) {
        orderMapper.cancelById(ordersCancelDTO);
    }

    @Override
    public OrderStatisticsVO statistics() {
        return orderMapper.statistics();
    }

    @Override
    public void completeById(Long id) {
        OrderVO  orderVO = orderMapper.queryById(id);
        if(!Objects.equals(orderVO.getPayStatus(), Orders.PAID)) {
            throw new IllegalArgumentException("订单未支付，不能完成");
        }
        orderMapper.completeById(id);
    }

    @Override
    public void rejectionById(OrdersRejectionDTO ordersRejectionDTO) {
        orderMapper.rejectionById(ordersRejectionDTO);
    }

    @Override
    public void confirmById(OrdersConfirmDTO ordersConfirmDTO) {
        ordersConfirmDTO.setStatus(Orders.CONFIRMED);
        orderMapper.confirmById(ordersConfirmDTO);
    }

    @Override
    public void deliveryById(Long id) {
        orderMapper.deliveryById(id);
    }
}
