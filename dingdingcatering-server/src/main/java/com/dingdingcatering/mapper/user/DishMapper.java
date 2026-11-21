package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userDishMapper")
@Mapper
public interface DishMapper {
    List<DishVO> getListByCategoryId(Long categoryId);

    Dish getById(Long id);

}