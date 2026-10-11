package com.dingdingcatering.controller.admin;

import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.WorkspaceService;
import com.dingdingcatering.vo.BusinessDataVO;
import com.dingdingcatering.vo.DishOverViewVO;
import com.dingdingcatering.vo.OrderOverViewVO;
import com.dingdingcatering.vo.SetMealOverViewVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作台数据概览控制器
 */
@RestController
@RequestMapping("/admin/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * 查询今日运营数据接口。
     *
     * @return 营业额、有效订单数、订单完成率、平均客单价、新增用户数
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData(){
        return Result.success(workspaceService.getBusinessData());
    }

    /**
     * 查询套餐总览接口。
     *
     * @return 已启售/已停售套餐数量
     */
    @GetMapping("/overviewSetmeals")
    public Result<SetMealOverViewVO> getOverviewSetmeals(){
        return Result.success(workspaceService.getOverviewSetmeal());
    }

    /**
     * 查询菜品总览接口。
     *
     * @return 已启售/已停售菜品数量
     */
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getOverviewDishes(){
        return Result.success(workspaceService.getOverviewDishes());
    }

    /**
     * 查询订单概览接口。
     *
     * @return 各状态订单数量统计
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOverviewOrders(){
        return Result.success(workspaceService.getOverviewOrders());
    }
}
