package com.dingdingcatering.controller.admin;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.enumeration.CacheType;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("adminStatusController")
@RequestMapping("/admin/shop")
@Slf4j
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = statusService.getStatus();
        log.info("获取店铺状态: {}", status);
        return Result.success(status);
    }

    @PutMapping("/{status}")
    @AutoClearCache(CacheType.SHOP_STATUS)
    public Result<Void> setStatus(@PathVariable Integer status) {
        statusService.updateStatus(status);
        log.info("更新店铺状态: {}", status);
        return Result.success();
    }
}
