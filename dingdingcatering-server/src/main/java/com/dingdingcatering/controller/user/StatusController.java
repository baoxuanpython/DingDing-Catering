package com.dingdingcatering.controller.user;

import com.dingdingcatering.annotation.AutoLogDTO;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.StatusService;
import org.springframework.web.bind.annotation.*;

@RestController("userStatusController")
@RequestMapping("/user/shop")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/status")
    @AutoLogDTO("获取店铺状态")
    public Result<Integer> getStatus() {
        Integer status = statusService.getStatus();
        return Result.success(status);
    }
}
