package com.dingdingcatering.controller.admin;

import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.StatusService;
import org.springframework.web.bind.annotation.*;

@RestController("adminStatusController")
@RequestMapping("/admin/shop")
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

    @PutMapping("/{status}")
    public Result<Void> setStatus(@PathVariable Integer status) {
        statusService.updateStatus(status);
        return Result.success();
    }
}
