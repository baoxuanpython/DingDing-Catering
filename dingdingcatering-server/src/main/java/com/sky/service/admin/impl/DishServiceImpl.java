package com.dingdingcatering.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.dto.DishDTO;
import com.dingdingcatering.dto.DishPageQueryDTO;
import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.entity.DishFlavor;
import com.dingdingcatering.exception.DeletionNotAllowedException;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.DishService;
import com.dingdingcatering.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;

    public DishServiceImpl(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        try (Page<DishVO> page = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize())) {
            List<DishVO> list = dishMapper.pageQuery(dishPageQueryDTO);
            return new PageResult<>(page.getTotal(), list);
        }
    }

    @Override
    public DishVO getById(Long id) {
        List<DishVO> list = dishMapper.getById(id);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<DishVO> getList(Long categoryId) {
        return dishMapper.getListByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.addDish(dish);

        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));

            dishMapper.addDishFlavor(flavors);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDish(String ids) {


        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        if (idList.isEmpty()) {
            return;
        }
        List<String> nameList = dishMapper.queryStatusByIds(idList);
        if (!nameList.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE + "ï¼? + String.join("ã€?, nameList));
        }
        List<HashMap<String, String>> setmealList = dishMapper.querySetmealByids(idList);
        if (!setmealList.isEmpty()) {
            String detail = setmealList.stream()
                    .map(map -> map.get("name") + "ã€? + map.get("setmeal_names") + "ã€?)
                    .collect(Collectors.joining("ï¼?));
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL + "ï¼? + detail);
        }

        dishMapper.deleteFlavorByDishIds(idList);

        dishMapper.deleteByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.updateDish(dish);

        Long dishId = dish.getId();

        dishMapper.deleteFlavorByDishIds(Collections.singletonList(dishId));

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishMapper.addDishFlavor(flavors);
        }
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        dishMapper.updateStatus(status, id);
    }
}
