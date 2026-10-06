package com.dingdingcatering.controller.user;

import com.dingdingcatering.annotation.AutoLogDTO;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.ShopConfigService;
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
