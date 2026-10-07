package com.dingdingcatering.service.user;

import com.dingdingcatering.entity.Dish;

import java.util.List;

public interface DishService {
    List<Dish> list(Integer categoryId);
}
