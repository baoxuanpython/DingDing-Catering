package com.dingdingcatering.controller.admin;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.enumeration.CacheType;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺营业状态控制器（管理端）
 */
@RestController("adminStatusController")
@RequestMapping("/admin/shop")
@Slf4j
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * 获取店铺营业状态接口。
     *
     * @return 店铺状态（1-营业中，0-打烊中）
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = statusService.getStatus();
        log.info("获取店铺营业状态: {}", status);
        return Result.success(status);
    }

    /**
     * 设置店铺营业状态接口。
     * <p>
     * 切换状态后自动清除相关缓存（菜品/套餐列表缓存）。
     *
     * @param status 目标状态（1-营业中，0-打烊中）
     * @return 设置成功提示
     */
    @PutMapping("/{status}")
    @AutoClearCache(CacheType.SHOP_STATUS)
    public Result<Void> setStatus(@PathVariable Integer status) {
        statusService.updateStatus(status);
        log.info("更新店铺营业状态: {}", status);
        return Result.success();
    }
}
