package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper {

    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @AutoFill
    void addDish(Dish dish);

    void addDishFlavor(List<DishFlavor> flavorList);
}
