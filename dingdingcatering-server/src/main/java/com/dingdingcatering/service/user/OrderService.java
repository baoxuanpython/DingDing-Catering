package com.dingdingcatering.service.user;

import com.dingdingcatering.dto.OrdersPaymentDTO;
import com.dingdingcatering.dto.OrdersSubmitDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.OrderSubmitVO;
import com.dingdingcatering.vo.OrderVO;
import com.dingdingcatering.vo.PaymentSuccessVO;

/**
 * C端订单服务接口
 */
public interface OrderService {
    /**
     * 催单（通过WebSocket推送）。
     *
     * @param id 订单ID
     */
    void sendReminder(Long id);

    /**
     * 提交订单。
     *
     * @param ordersSubmitDTO 订单提交信息
     * @return 订单提交结果
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付。
     *
     * @param ordersPaymentDTO 订单号及付款方式
     * @return 支付成功结果
     */
    PaymentSuccessVO payment(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 查询历史订单（分页）。
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @param status   订单状态筛选
     * @return 订单分页数据
     */
    PageResult<OrderVO> getHistoryOrders(Integer page, Integer pageSize, Integer status);

    /**
     * 查询订单详情。
     *
     * @param id 订单ID
     * @return 订单详情
     */
    OrderVO getOrderDetail(Long id);

    /**
     * 再来一单（将历史订单菜品加入购物车）。
     *
     * @param id 历史订单ID
     */
    void repetition(Long id);

    /**
     * 用户取消订单。
     *
     * @param id 订单ID
     */
    void cancel(Long id);
}
