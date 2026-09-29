package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;
    public DishServiceImpl(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        try (Page<DishVO> page = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize())){
            List<DishVO> list = dishMapper.pageQuery(dishPageQueryDTO);
            return new PageResult<>(page.getTotal(), list);
        }
    }

    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        List<DishFlavor> flavorList = new ArrayList<>();
        BeanUtils.copyProperties(dishDTO, dish);
        BeanUtils.copyProperties(dishDTO.getFlavors(), flavorList);
        dishMapper.addDish(dish);
        dishMapper.addDishFlavor(flavorList);
    }
}
