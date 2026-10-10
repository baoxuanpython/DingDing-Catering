package com.dingdingcatering.service.user;

import com.dingdingcatering.dto.OrdersPaymentDTO;
import com.dingdingcatering.dto.OrdersSubmitDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.OrderSubmitVO;
import com.dingdingcatering.vo.OrderVO;
import com.dingdingcatering.vo.PaymentSuccessVO;

public interface OrderService {
    void sendReminder(Long id);

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    PaymentSuccessVO payment(OrdersPaymentDTO ordersPaymentDTO);

    PageResult<OrderVO> getHistoryOrders(Integer page, Integer pageSize, Integer status);

    OrderVO getOrderDetail(Long id);

    void repetition(Long id);

    void cancel(Long id);
}
