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

@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/reminder/{id}")
    public Result<Void> sendReminder(@PathVariable Long id) {
        orderService.sendReminder(id);
        return Result.success();
    }

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        return Result.success(orderService.submitOrder(ordersSubmitDTO));
    }

    @PutMapping("/payment")
    public Result<PaymentSuccessVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        return Result.success(orderService.payment(ordersPaymentDTO));
    }

    @GetMapping("/historyOrders")
    public Result<PageResult<OrderVO>> getHistoryOrders(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) Integer status) {
        return Result.success(orderService.getHistoryOrders(page, pageSize, status));
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @PostMapping("/repetition/{id}")
    public Result<Void> repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.success();
    }
}
