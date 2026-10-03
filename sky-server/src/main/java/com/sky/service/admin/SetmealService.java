package com.sky.service.admin;

import com.sky.dto.SetMealDTO;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetMealVO;


public interface SetmealService {

    PageResult<SetMealVO> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO);

    void addSetmeal(SetMealDTO setmealDTO);

    void deleteSetmeal(String ids);

    SetMealVO getSetmealById(Long id);

    void updateStatus(Integer status, Long id);

    void updateSetmeal(SetMealDTO setmealDTO);
}
