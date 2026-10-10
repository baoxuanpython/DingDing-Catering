package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("userDishMapper")
@Mapper
public interface DishMapper {
    List<Dish> getListByCategoryId(Integer categoryId);

    Dish getById(Long id);

}
