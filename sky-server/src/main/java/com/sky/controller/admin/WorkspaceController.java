package com.sky.controller.admin;

import com.sky.annotation.AutoLogDTO;
import com.sky.result.Result;
import com.sky.service.admin.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetMealOverViewVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping("/businessData")
    @AutoLogDTO("获取业务数据")
    public Result<BusinessDataVO> getBusinessData(){
        return Result.success(workspaceService.getBusinessData());
    }
    @GetMapping("/overviewSetmeals")
    @AutoLogDTO("获取套餐数据")
    public Result<SetMealOverViewVO> getOverviewSetmeals(){
        return Result.success(workspaceService.getOverviewSetmeals());
    }
    @GetMapping("/overviewDishes")
    @AutoLogDTO("获取菜品数据")
    public Result<DishOverViewVO> getOverviewDishes(){
        return Result.success(workspaceService.getOverviewDishes());
    }
    @GetMapping("/overviewOrders")
    @AutoLogDTO("获取订单数据")
    public Result<OrderOverViewVO> getOverviewOrders(){
        return Result.success(workspaceService.getOverviewOrders());
    }
}
