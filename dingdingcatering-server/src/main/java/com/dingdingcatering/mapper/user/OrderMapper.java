package com.dingdingcatering.mapper.user;

import com.dingdingcatering.dto.OrdersPaymentDTO;
import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.Orders;
import com.dingdingcatering.entity.ShoppingCart;
import com.dingdingcatering.vo.OrderSubmitVO;
import com.dingdingcatering.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userOrderMapper")
@Mapper
public interface OrderMapper {
    void insertOrder(Orders orders);

    void paySuccess(OrdersPaymentDTO ordersPaymentDTO);

    List<OrderVO> getHistoryOrders(Long userId, Integer status);

    List<OrderDetail> getDetailList(List<Long> orderIds);

    void insertOrderDetail(List<OrderDetail> orderDetailList);

    Orders getOrdersById(Long id, Long userId);

    void cancelOrder(Long id, Long userId);
}
