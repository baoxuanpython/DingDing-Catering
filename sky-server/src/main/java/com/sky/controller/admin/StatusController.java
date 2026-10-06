package com.dingdingcatering.controller.admin;

import com.dingdingcatering.annotation.AutoLogDTO;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.ShopConfigService;
import org.springframework.web.bind.annotation.*;

@RestController("adminStatusController")
@RequestMapping("/admin/shop")
public class StatusController {
    private final ShopConfigService shopConfigService;

    public StatusController(ShopConfigService shopConfigService) {
        this.shopConfigService = shopConfigService;
    }

    @GetMapping("/status")
    @AutoLogDTO("获取店铺状�?)
    public Result<Integer> getStatus() {
        Integer status = shopConfigService.getShopStatus();
        return Result.success(status);
    }

    @AutoLogDTO("设置店铺状�?)
    @PutMapping("/{status}")
    public Result<Void> setStatus(@PathVariable Integer status) {
        shopConfigService.updateShopStatus(status);
        return Result.success();
    }
}
