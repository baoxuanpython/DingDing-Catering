package com.dingdingcatering.controller.user;

import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.StatusService;
import org.springframework.web.bind.annotation.*;

/**
 * C端店铺营业状态控制器
 */
@RestController("userStatusController")
@RequestMapping("/user/shop")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * 获取店铺营业状态接口。
     * <p>
     * C端用户下单前需校验店铺是否营业中。
     *
     * @return 店铺状态（1-营业中，0-打烊中）
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = statusService.getStatus();
        return Result.success(status);
    }
}
