package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.enumeration.CacheType;
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
@Service("adminDishServiceImpl")
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;

    public DishServiceImpl(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.debug("分页查询菜品: page={}, pageSize={}, name={}, categoryId={}",
                dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize(),
                dishPageQueryDTO.getName(), dishPageQueryDTO.getCategoryId());
        try (Page<DishVO> page = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize())) {
            List<DishVO> list = dishMapper.pageQuery(dishPageQueryDTO);
            log.debug("分页查询菜品完成: total={}", page.getTotal());
            return new PageResult<>(page.getTotal(), list);
        }
    }

    @Override
    public DishVO getById(Long id) {
        log.debug("根据ID查询菜品: id={}", id);
        List<DishVO> list = dishMapper.getById(id);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<DishVO> getList(Long categoryId) {
        log.debug("根据分类ID查询菜品列表: categoryId={}", categoryId);
        return dishMapper.getListByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AutoClearCache(CacheType.DISH)
    public void addDish(DishDTO dishDTO) {
        log.info("新增菜品: name={}, categoryId={}", dishDTO.getName(), dishDTO.getCategoryId());
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishMapper.addDishFlavor(flavors);
            log.info("新增菜品口味: dishId={}, flavorCount={}", dishId, flavors.size());
        }
        log.info("新增菜品成功: id={}", dishId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AutoClearCache({CacheType.DISH, CacheType.CATEGORY})
    public void deleteDish(String ids) {
        log.info("删除菜品: ids={}", ids);
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
        if (idList.isEmpty()) {
            log.warn("删除菜品: ID列表为空");
            return;
        }
        List<String> nameList = dishMapper.queryStatusByIds(idList);
        if (!nameList.isEmpty()) {
            log.warn("删除菜品失败: 菜品已起售, names={}", nameList);
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE + " " + String.join("、", nameList));
        }
        List<HashMap<String, String>> setmealList = dishMapper.querySetmealByIds(idList);
        if (!setmealList.isEmpty()) {
            String detail = setmealList.stream()
                    .map(map -> map.get("name") + "：" + map.get("setmeal_names"))
                    .collect(Collectors.joining("、"));
            log.warn("删除菜品失败: 菜品被套餐关联, detail={}", detail);
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL + " " + detail);
        }
        dishMapper.deleteFlavorByDishIds(idList);
        dishMapper.deleteByIds(idList);
        log.info("删除菜品成功: idCount={}", idList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AutoClearCache(CacheType.DISH)
    public void updateDish(DishDTO dishDTO) {
        log.info("更新菜品: id={}, name={}", dishDTO.getId(), dishDTO.getName());
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDish(dish);
        Long dishId = dish.getId();
        dishMapper.deleteFlavorByDishIds(Collections.singletonList(dishId));
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishMapper.addDishFlavor(flavors);
            log.info("更新菜品口味: dishId={}, flavorCount={}", dishId, flavors.size());
        }
        log.info("更新菜品成功: id={}", dishId);
    }

    @Override
    @AutoClearCache(CacheType.DISH)
    public void updateStatus(Integer status, Long id) {
        log.info("更新菜品状态: id={}, status={}", id, status);
        dishMapper.updateStatus(status, id);
    }
}
