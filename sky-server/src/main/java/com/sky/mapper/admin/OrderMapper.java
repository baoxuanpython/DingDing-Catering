package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.dto.OrdersCancelDTO;
import com.dingdingcatering.dto.OrdersConfirmDTO;
import com.dingdingcatering.dto.OrdersPageQueryDTO;
import com.dingdingcatering.dto.OrdersRejectionDTO;
import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.vo.OrderStatisticsVO;
import com.dingdingcatering.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<OrderVO> queryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    List<OrderDetail> queryOrderDetails(@Param("list") List<Long> orderIds);

    OrderVO queryById(Long id);

    void cancelById(OrdersCancelDTO ordersCancelDTO);

    OrderStatisticsVO statistics();

    void completeById(Long id);

    void rejectionById(OrdersRejectionDTO ordersRejectionDTO);

    void confirmById(OrdersConfirmDTO ordersConfirmDTO);

    void deliveryById(Long id);
}
