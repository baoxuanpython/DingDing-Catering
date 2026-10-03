package com.sky.service.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    PageResult<OrderVO> pageQueryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO queryOrderDetails(Long id);

    void cancelById(OrdersCancelDTO ordersCancelDTO);

    OrderStatisticsVO statistics();

    void completeById(Long id);

    void rejectionById(OrdersRejectionDTO ordersRejectionDTO);

    void confirmById(OrdersConfirmDTO ordersConfirmDTO);

    void deliveryById(Long id);
}
