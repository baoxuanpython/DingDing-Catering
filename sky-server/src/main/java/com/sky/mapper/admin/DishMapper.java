package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.annotation.AutoFill;
import com.dingdingcatering.dto.DishPageQueryDTO;
import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.entity.DishFlavor;
import com.dingdingcatering.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface DishMapper {

    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    List<DishVO> getById(Long id);

    List<DishVO> getListByCategoryId(Long categoryId);

    @AutoFill
    void addDish(Dish dish);

    void addDishFlavor(@Param("flavors") List<DishFlavor> flavors);

    void deleteByIds(@Param("ids") List<Long> ids);

    void deleteFlavorByDishIds(@Param("dishIds") List<Long> dishIds);

    @AutoFill
    void updateDish(Dish dish);

    void updateStatus(Integer status, Long id);

    Integer queryByCategoryId(@Param("categoryId") Long categoryId);

    List<String> queryByStatus(@Param("dishIds") List<Long> dishIds);

    List<String> queryStatusByIds(List<Long> idList);

    List<HashMap<String, String>> querySetmealByids(List<Long> idList);
}
