package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.annotation.AutoFill;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.entity.SetMealDish;
import com.dingdingcatering.enumeration.OperationType;
import com.dingdingcatering.vo.SetMealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("adminSetMealMapper")
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
