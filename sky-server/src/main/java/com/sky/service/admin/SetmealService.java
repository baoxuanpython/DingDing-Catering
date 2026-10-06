package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.SetMealDTO;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.SetMealVO;


public interface SetmealService {

    PageResult<SetMealVO> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO);

    void addSetmeal(SetMealDTO setmealDTO);

    void deleteSetmeal(String ids);

    SetMealVO getSetmealById(Long id);

    void updateStatus(Integer status, Long id);

    void updateSetmeal(SetMealDTO setmealDTO);
}
