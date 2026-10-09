package com.dingdingcatering.controller.admin;

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
    public Result<PageResult<OrderVO>> pageQueryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult<OrderVO> pageResult = orderService.pageQueryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/details/{id}")
    public Result<OrderVO> queryOrderDetails(@PathVariable Long id) {
        OrderVO orderVO = orderService.queryOrderDetails(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel")
    public Result<Void> cancelById(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancelById(ordersCancelDTO);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO statistics = orderService.statistics();
        return Result.success(statistics);
    }
    @PutMapping("/complete/{id}")
    public Result<Void> completeById(@PathVariable Long id) {
        orderService.completeById(id);
        return Result.success();
    }
    @PutMapping("/rejection")
    public Result<Void> rejectionById(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejectionById(ordersRejectionDTO);
        return Result.success();
    }
    @PutMapping("/confirm")
    public Result<Void> confirmById(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirmById(ordersConfirmDTO);
        return Result.success();
    }
    @PutMapping("/delivery/{id}")
    public Result<Void> deliveryById(@PathVariable Long id) {
        orderService.deliveryById(id);
        return Result.success();
    }
}
