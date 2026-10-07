package com.dingdingcatering.controller.admin;

import com.dingdingcatering.annotation.AutoLogDTO;
import com.dingdingcatering.dto.OrdersCancelDTO;
import com.dingdingcatering.dto.OrdersConfirmDTO;
import com.dingdingcatering.dto.OrdersPageQueryDTO;
import com.dingdingcatering.dto.OrdersRejectionDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.OrderService;
import com.dingdingcatering.vo.OrderStatisticsVO;
import com.dingdingcatering.vo.OrderVO;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/conditionSearch")
    @AutoLogDTO("订单查询")
    public Result<PageResult<OrderVO>> pageQueryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult<OrderVO> pageResult = orderService.pageQueryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/details/{id}")
    @AutoLogDTO("订单详情查询")
    public Result<OrderVO> queryOrderDetails(@PathVariable Long id) {
        OrderVO orderVO = orderService.queryOrderDetails(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel")
    @AutoLogDTO("订单取消")
    public Result<Void> cancelById(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancelById(ordersCancelDTO);
        return Result.success();
    }

    @GetMapping("/statistics")
    @AutoLogDTO("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO statistics = orderService.statistics();
        return Result.success(statistics);
    }
    @PutMapping("/complete/{id}")
    @AutoLogDTO("订单完成")
    public Result<Void> completeById(@PathVariable Long id) {
        orderService.completeById(id);
        return Result.success();
    }
    @PutMapping("/rejection")
    @AutoLogDTO("订单拒绝")
    public Result<Void> rejectionById(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejectionById(ordersRejectionDTO);
        return Result.success();
    }
    @PutMapping("/confirm")
    @AutoLogDTO("订单接收")
    public Result<Void> confirmById(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirmById(ordersConfirmDTO);
        return Result.success();
    }
    @PutMapping("/delivery/{id}")
    @AutoLogDTO("订单配送")
    public Result<Void> deliveryById(@PathVariable Long id) {
        orderService.deliveryById(id);
        return Result.success();
    }
}
