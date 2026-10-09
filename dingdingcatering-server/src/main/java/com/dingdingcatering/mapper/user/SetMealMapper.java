package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("userSetMealMapper")
public interface SetMealMapper {
    List<Setmeal> list(Integer categoryId);

    List<DishItemVO> dish(Long id);
}

