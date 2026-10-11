package com.dingdingcatering.controller.user;

import com.dingdingcatering.dto.OrdersPaymentDTO;
import com.dingdingcatering.dto.OrdersSubmitDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.OrderService;
import com.dingdingcatering.vo.OrderSubmitVO;
import com.dingdingcatering.vo.OrderVO;
import com.dingdingcatering.vo.PaymentSuccessVO;
import org.springframework.web.bind.annotation.*;

/**
 * C端订单控制器
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 催单接口。
     * <p>
     * 通过WebSocket向管理端推送催单提醒。
     *
     * @param id 用于定位目标订单的主键标识
     * @return 催单成功提示
     */
    @GetMapping("/reminder/{id}")
    public Result<Void> sendReminder(@PathVariable Long id) {
        orderService.sendReminder(id);
        return Result.success();
    }

    /**
     * 提交订单接口。
     *
     * @param ordersSubmitDTO 订单提交信息（含地址、支付方式、备注等）
     * @return 订单提交结果（含订单号、金额）
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        return Result.success(orderService.submitOrder(ordersSubmitDTO));
    }

    /**
     * 订单支付接口。
     *
     * @param ordersPaymentDTO 订单号及付款方式
     * @return 支付成功结果（含预计送达时间）
     */
    @PutMapping("/payment")
    public Result<PaymentSuccessVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        return Result.success(orderService.payment(ordersPaymentDTO));
    }

    /**
     * 查询历史订单接口。
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @param status   订单状态筛选（可选）
     * @return 用户历史订单分页数据
     */
    @GetMapping("/historyOrders")
    public Result<PageResult<OrderVO>> getHistoryOrders(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) Integer status) {
        return Result.success(orderService.getHistoryOrders(page, pageSize, status));
    }

    /**
     * 查询订单详情接口。
     *
     * @param id 用于查询订单的主键标识
     * @return 订单详情（含菜品列表）
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    /**
     * 再来一单接口。
     * <p>
     * 将历史订单中的菜品重新加入购物车。
     *
     * @param id 历史订单的主键标识
     * @return 操作成功提示
     */
    @PostMapping("/repetition/{id}")
    public Result<Void> repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 用户取消订单接口。
     *
     * @param id 用于定位目标订单的主键标识
     * @return 取消成功提示
     */
    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.success();
    }
}
