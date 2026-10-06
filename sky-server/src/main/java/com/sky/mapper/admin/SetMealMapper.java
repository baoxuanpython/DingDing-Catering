package com.sky.mapper.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetMealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetMealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface SetMealMapper {
    List<SetMealVO> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO);

    @AutoFill
    void addSetMeal(Setmeal setmeal);

    void addSetMealDishes(@Param("setMealDishes") List<SetMealDish> setMealDishes);

    void deleteSetMeal(List<Long> idList);

    void deleteSetMealDishes(List<Long> idList);

    SetMealVO getSetMealById(Long id);

    void updateStatus(Integer status, Long id);

    @AutoFill(OperationType.UPDATE)
    void updateSetMeal(Setmeal setmeal);

    Integer queryByCategoryId(@Param("categoryId") Long categoryId);

    List<Long> queryDishesId(Long id);

    List<String> queryStatusByIds(List<Long> idList);

}
