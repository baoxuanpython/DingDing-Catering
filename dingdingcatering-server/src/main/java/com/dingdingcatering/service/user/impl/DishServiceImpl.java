package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.mapper.user.DishMapper;
import com.dingdingcatering.service.user.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        log.debug("缓存未命中，查询菜品列表: categoryId={}", categoryId);
        List<Dish> result = dishMapper.getListByCategoryId(categoryId);
        log.debug("查询菜品列表完成: categoryId={}, count={}", categoryId, result.size());
        return result;
    }
}
