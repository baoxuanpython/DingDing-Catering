package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.OrdersCancelDTO;
import com.dingdingcatering.dto.OrdersConfirmDTO;
import com.dingdingcatering.dto.OrdersPageQueryDTO;
import com.dingdingcatering.dto.OrdersRejectionDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.OrderStatisticsVO;
import com.dingdingcatering.vo.OrderVO;

/**
 * 订单管理服务接口（管理端）
 */
public interface OrderService {

    /**
     * 订单条件搜索（分页）。
     *
     * @param ordersPageQueryDTO 搜索参数
     * @return 订单分页数据
     */
    PageResult<OrderVO> pageQueryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询订单详情。
     *
     * @param number 订单号
     * @return 订单详情
     */
    OrderVO queryOrderDetails(Long number);

    /**
     * 取消订单。
     *
     * @param ordersCancelDTO 含订单ID及取消原因
     */
    void cancelById(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单统计（待接单/待派送/派送中数量）。
     *
     * @return 统计数据
     */
    OrderStatisticsVO statistics();

    /**
     * 完成订单。
     *
     * @param id 订单ID
     */
    void completeById(Long id);

    /**
     * 拒单。
     *
     * @param ordersRejectionDTO 含订单ID及拒绝原因
     */
    void rejectionById(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 接单。
     *
     * @param ordersConfirmDTO 含订单ID及目标状态
     */
    void confirmById(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 派送订单。
     *
     * @param id 订单ID
     */
    void deliveryById(Long id);
}
