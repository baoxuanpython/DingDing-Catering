package com.dingdingcatering.service.user.Impl;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.mapper.user.SetmealMapper;
import com.dingdingcatering.service.user.SetmealService;
import com.dingdingcatering.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userSetmealServiceImpl")
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    private final SetmealMapper setmealMapper;
    public SetmealServiceImpl(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    @Override
    public List<Setmeal> list(Integer categoryId) {
        return setmealMapper.list(categoryId);
    }

    @Override
    public List<DishItemVO> dish(Long id) {
        return setmealMapper.dish(id);
    }
}
