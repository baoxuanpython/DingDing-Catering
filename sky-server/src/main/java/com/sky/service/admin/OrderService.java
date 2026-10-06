package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.OrdersCancelDTO;
import com.dingdingcatering.dto.OrdersConfirmDTO;
import com.dingdingcatering.dto.OrdersPageQueryDTO;
import com.dingdingcatering.dto.OrdersRejectionDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.OrderStatisticsVO;
import com.dingdingcatering.vo.OrderVO;

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
