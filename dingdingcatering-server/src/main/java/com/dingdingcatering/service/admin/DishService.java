package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.DishDTO;
import com.dingdingcatering.dto.DishPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.DishVO;

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
