package com.dingdingcatering.mapper.user;

import com.dingdingcatering.dto.OrdersPaymentDTO;
import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.Orders;
import com.dingdingcatering.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    void cancelOrderWithRefund(Long id, Long userId, String cancelReason);

    void cancelOrderWithoutRefund(Long id, Long userId, String cancelReason);

    OrderVO getOrdersByNumber(Long number, Long userId, Integer status);

    /**
     * 根据订单号查询订单主键
     *
     * @param number 订单号
     * @return 订单主键，订单不存在时返回 null
     */
    Long getIdByNumber(@Param("number") String number);
}
