package com.sky.controller.user;

import com.sky.annotation.AutoLogDTO;
import com.sky.result.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userStatusController")
@RequestMapping("/user/shop")
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
}


