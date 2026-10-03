package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetMealDTO;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.entity.SetMealDish;
import com.sky.entity.Setmeal;
import com.sky.mapper.admin.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.admin.SetmealService;
import com.sky.vo.SetMealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    private final SetMealMapper setmealMapper;

    public SetmealServiceImpl(SetMealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    @Override
    public PageResult<SetMealVO> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO) {
        try (Page<SetMealVO> page = PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize())) {
            List<SetMealVO> list = setmealMapper.pageQuery(setmealPageQueryDTO);
            return new PageResult<>(page.getTotal(), list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSetmeal(SetMealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.addSetMeal(setmeal);
        List<SetMealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if (setMealDishes == null || setMealDishes.isEmpty()) {
            return;
        }
        setMealDishes.forEach(setMealDish -> {
            setMealDish.setSetmealId(setmeal.getId());
        });
        setmealMapper.addSetMealDishes(setMealDishes);
    }

    @Override
    public void deleteSetmeal(String ids) {
        List<Long> idList = Arrays.stream(ids.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(Long::parseLong).toList();
        if (idList.isEmpty()) {
            return;
        }
        setmealMapper.deleteSetMeal(idList);
        setmealMapper.deleteSetMealDishes(idList);
    }

    @Override
    public SetMealVO getSetmealById(Long id) {
        return setmealMapper.getSetMealById(id);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        setmealMapper.updateStatus(status, id);
    }

    @Override
    public void updateSetmeal(SetMealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetMeal(setmeal);
        setmealMapper.deleteSetMealDishes(List.of(setmeal.getId()));
        List<SetMealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if (setMealDishes == null || setMealDishes.isEmpty()) {
            return;
        }
        setMealDishes.forEach(setMealDish -> {
            setMealDish.setSetmealId(setmeal.getId());
        });
        log.info("setMealDishes: {}", setMealDishes);
        setmealMapper.addSetMealDishes(setMealDishes);
    }
}
