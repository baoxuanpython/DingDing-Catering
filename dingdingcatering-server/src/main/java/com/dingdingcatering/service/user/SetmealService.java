package com.dingdingcatering.service.user;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.vo.DishItemVO;

import java.util.List;

public interface SetmealService {
    List<Setmeal> list(Integer categoryId);

    List<DishItemVO> dish(Long id);
}
