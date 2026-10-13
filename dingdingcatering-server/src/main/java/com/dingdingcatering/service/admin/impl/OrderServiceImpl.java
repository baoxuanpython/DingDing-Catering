package com.dingdingcatering.service.admin.impl;


import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.dto.OrdersCancelDTO;
import com.dingdingcatering.dto.OrdersConfirmDTO;
import com.dingdingcatering.dto.OrdersPageQueryDTO;
import com.dingdingcatering.dto.OrdersRejectionDTO;
import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.Orders;
import com.dingdingcatering.exception.OrderBusinessException;
import com.dingdingcatering.mapper.admin.OrderMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.OrderService;
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
            Orders.CANCELLED,             // 6 - 已取消
            Orders.REFUNDED             // 7 - 已退款
    );
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
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
                // 使用工具类填充订单详情和菜品信息
                enrichOrderVOListFromAdmin(orderList, details, OrderVO::getId);
            }
            log.debug("分页查询订单完成: total={}", page.getTotal());
            return new PageResult<>(page.getTotal(), orderList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO queryOrderDetails(Long number) {
        log.debug("查询订单详情: number={}", number);
        OrderVO orderVO = orderMapper.queryById(number);
        if (orderVO == null) {
            log.warn("查询订单详情失败: 订单不存在, number={}", number);
            throw new OrderBusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!VALID_ORDER_STATUSES.contains(orderVO.getStatus())) {
            log.warn("查询订单详情失败: 订单状态无效, number={}, status={}", number, orderVO.getStatus());
            throw new OrderBusinessException(ErrorCode.ORDER_STATUS_ERROR);
        }
        List<OrderDetail> orderDetailList = orderMapper.queryOrderDetails(Collections.singletonList(number));
        orderVO.setOrderDetailList(orderDetailList);
        orderVO.setOrderDishes(orderDetailList.stream().map(OrderDetail::getName).collect(Collectors.joining("、")));
        return orderVO;
    }

    @Override
    public void cancelById(OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单: id={}, reason={}", ordersCancelDTO.getId(), ordersCancelDTO.getCancelReason());
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
        if (!Objects.equals(orderVO.getPayStatus(), Orders.PAID)) {
            log.warn("完成订单失败: 订单未支付, id={}, payStatus={}", id, orderVO.getPayStatus());
            throw new IllegalArgumentException("订单未支付，不能完成");
        }
        orderMapper.completeById(id);
        log.info("完成订单成功: id={}", id);
    }

    @Override
    public void rejectionById(OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒绝订单: id={}, reason={}", ordersRejectionDTO.getId(), ordersRejectionDTO.getRejectionReason());
        orderMapper.rejectionById(ordersRejectionDTO);
        log.info("拒绝订单成功: id={}", ordersRejectionDTO.getId());
    }

    @Override
    public void confirmById(OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单: id={}", ordersConfirmDTO.getId());
        ordersConfirmDTO.setStatus(Orders.CONFIRMED);
        orderMapper.confirmById(ordersConfirmDTO);
        log.info("接单成功: id={}", ordersConfirmDTO.getId());
    }

    @Override
    public void deliveryById(Long id) {
        log.info("派送订单: id={}", id);
        orderMapper.deliveryById(id);
        log.info("派送订单成功: id={}", id);
    }
}
