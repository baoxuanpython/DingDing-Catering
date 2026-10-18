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

/**
 * 订单管理控制器（管理端）
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 订单条件搜索接口。
     * <p>
     * 支持按订单号、手机号、状态、时间范围等条件筛选订单。
     *
     * @param ordersPageQueryDTO 订单搜索参数
     * @return 订单分页数据（含详情）
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult<OrderVO>> pageQueryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult<OrderVO> pageResult = orderService.pageQueryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情接口。
     *
     * @param number 用于查询订单的订单号
     * @return 订单详情（含菜品列表）
     */
    @GetMapping("/details/{number}")
    public Result<OrderVO> queryOrderDetails(@PathVariable String number) {
        OrderVO orderVO = orderService.queryOrderDetails(number);
        return Result.success(orderVO);
    }

    /**
     * 取消订单接口。
     *
     * @param ordersCancelDTO 含订单ID及取消原因
     * @return 取消成功提示
     */
    @PutMapping("/cancel")
    public Result<Void> cancelById(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancelById(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 订单统计接口。
     * <p>
     * 返回待接单、待派送、派送中、已完成、已取消、已退款的订单数量。
     *
     * @return 订单统计数据
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO statistics = orderService.statistics();
        return Result.success(statistics);
    }

    /**
     * 完成订单接口。
     *
     * @param id 用于定位目标订单的主键标识
     * @return 完成成功提示
     */
    @PutMapping("/complete/{id}")
    public Result<Void> completeById(@PathVariable Long id) {
        orderService.completeById(id);
        return Result.success();
    }

    /**
     * 拒单接口。
     *
     * @param ordersRejectionDTO 含订单ID及拒绝原因
     * @return 拒单成功提示
     */
    @PutMapping("/rejection")
    public Result<Void> rejectionById(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejectionById(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 接单接口。
     *
     * @param ordersConfirmDTO 含订单ID及目标状态
     * @return 接单成功提示
     */
    @PutMapping("/confirm")
    public Result<Void> confirmById(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirmById(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 派送订单接口。
     *
     * @param id 用于定位目标订单的主键标识
     * @return 派送成功提示
     */
    @PutMapping("/delivery/{id}")
    public Result<Void> deliveryById(@PathVariable Long id) {
        orderService.deliveryById(id);
        return Result.success();
    }
}