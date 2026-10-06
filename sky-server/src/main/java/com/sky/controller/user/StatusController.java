package com.sky.controller.user;

import com.sky.annotation.AutoLogDTO;
import com.sky.result.Result;
import com.sky.service.admin.ShopConfigService;
import org.springframework.web.bind.annotation.*;

@RestController("userStatusController")
@RequestMapping("/user/shop")
public class StatusController {
    private final ShopConfigService shopConfigService;

    public StatusController(ShopConfigService shopConfigService) {
        this.shopConfigService = shopConfigService;
    }

    @GetMapping("/status")
    @AutoLogDTO("获取店铺状态")
    public Result<Integer> getStatus() {
        Integer status = shopConfigService.getShopStatus();
        return Result.success(status);
    }
}
