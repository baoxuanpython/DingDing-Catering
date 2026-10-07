package com.dingdingcatering.service.user.Impl;

import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.mapper.user.DishMapper;
import com.dingdingcatering.service.user.DishService;
import lombok.extern.slf4j.Slf4j;
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
    public List<Dish> list(Integer categoryId) {
        return dishMapper.getListByCategoryId(categoryId);
    }
}
