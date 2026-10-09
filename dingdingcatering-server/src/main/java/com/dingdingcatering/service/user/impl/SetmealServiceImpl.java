package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.enumeration.CacheType;
import com.dingdingcatering.mapper.user.SetmealMapper;
import com.dingdingcatering.service.user.SetmealService;
import com.dingdingcatering.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "setmeal:category", key = "#categoryId", unless = "#result == null ||# result.isEmpty()")
    public List<Setmeal> list(Integer categoryId) {
        return setmealMapper.list(categoryId);
    }

    @Override
    @Cacheable(value = "setmeal:dish", key = "#id", unless = "#result == null ||# result.isEmpty()")
    public List<DishItemVO> dish(Long id) {
        return setmealMapper.dish(id);
    }
}
