package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.vo.DishItemVO;
import com.dingdingcatering.vo.SetMealVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userSetmealMapper")
@Mapper
public interface SetmealMapper {
    List<Setmeal> list(Integer categoryId);

    List<DishItemVO> dish(Long id);
}
