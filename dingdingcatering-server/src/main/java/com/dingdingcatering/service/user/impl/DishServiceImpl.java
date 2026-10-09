package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.constant.RedisConstant;
import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.mapper.user.DishMapper;
import com.dingdingcatering.service.user.DishService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("userDishServiceImpl")
@Slf4j
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;

    public DishServiceImpl(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Override
    @Cacheable(value = "dish",key = "#categoryId" ,unless = "#result == null || #result.isEmpty()")
    public List<Dish> list(Integer categoryId) {
        log.info("缓存未命中，查询菜品列表，categoryId: {}", categoryId);
        return dishMapper.getListByCategoryId(categoryId);
    }
}



