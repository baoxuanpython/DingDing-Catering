package com.sky.service.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    DishVO getById(Long id);

    List<DishVO> getList(Long categoryId);

    void addDish(DishDTO dishDTO);

    void deleteDish(String ids);

    void updateDish(DishDTO dishDTO);

    void updateStatus(Integer status, Long id);
}
