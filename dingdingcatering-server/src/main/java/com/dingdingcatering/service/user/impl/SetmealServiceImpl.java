package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.mapper.user.SetMealMapper;
import com.dingdingcatering.service.user.SetmealService;
import com.dingdingcatering.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userSetmealServiceImpl")
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    private final SetMealMapper setmealMapper;

    public SetmealServiceImpl(@Qualifier("userSetMealMapper") SetMealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    @Override
    @Cacheable(value = "setmeal:category", key = "#categoryId", unless = "#result == null ||# result.isEmpty()")
    public List<Setmeal> list(Integer categoryId) {
        log.debug("缓存未命中，查询套餐列表: categoryId={}", categoryId);
        List<Setmeal> result = setmealMapper.list(categoryId);
        log.debug("查询套餐列表完成: categoryId={}, count={}", categoryId, result.size());
        return result;
    }

    @Override
    @Cacheable(value = "setmeal:dish", key = "#id", unless = "#result == null ||# result.isEmpty()")
    public List<DishItemVO> dish(Long id) {
        log.debug("缓存未命中，查询套餐菜品: setmealId={}", id);
        List<DishItemVO> result = setmealMapper.dish(id);
        log.debug("查询套餐菜品完成: setmealId={}, count={}", id, result.size());
        return result;
    }
}
