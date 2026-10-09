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

@RestController
@RequestMapping("/admin/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData(){
        return Result.success(workspaceService.getBusinessData());
    }
    @GetMapping("/overviewSetmeals")
    public Result<SetMealOverViewVO> getOverviewSetmeals(){
        return Result.success(workspaceService.getOverviewSetmeal());
    }
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getOverviewDishes(){
        return Result.success(workspaceService.getOverviewDishes());
    }
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOverviewOrders(){
        return Result.success(workspaceService.getOverviewOrders());
    }
}
