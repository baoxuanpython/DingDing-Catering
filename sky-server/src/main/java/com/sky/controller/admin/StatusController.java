package com.sky.controller.admin;

import com.sky.annotation.AutoLogDTO;
import com.sky.result.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminStatusController")
@RequestMapping("/admin/shop")
public class StatusController {
    private final String SHOP_STATUS = "SHOP_STATUS";
    private final RedisTemplate<String, Object> redisTemplate;

    public StatusController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/status")
    @AutoLogDTO("获取店铺状态")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        return Result.success(status);
    }

    @AutoLogDTO("设置店铺状态")
    @PutMapping("/{status}")
    public Result<Void> setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set(SHOP_STATUS, status);
        return Result.success();
    }
}


