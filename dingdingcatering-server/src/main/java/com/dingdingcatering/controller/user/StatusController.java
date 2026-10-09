package com.dingdingcatering.controller.user;

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
    public Result<Integer> getStatus() {
        Integer status = statusService.getStatus();
        return Result.success(status);
    }
}
