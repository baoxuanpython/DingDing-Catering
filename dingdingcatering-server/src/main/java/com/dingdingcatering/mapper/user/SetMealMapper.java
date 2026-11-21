package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.SetMealDish;
import com.dingdingcatering.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("userSetMealMapper")
public interface SetMealMapper {
    List<Setmeal> list(Long categoryId);

    List<SetMealDish> dish(Long id);

    Setmeal getById(Long id);

    List<SetMealDish> queryDishesBySetmealIds(@Param("setmealIds") List<Long> setmealIds);

}