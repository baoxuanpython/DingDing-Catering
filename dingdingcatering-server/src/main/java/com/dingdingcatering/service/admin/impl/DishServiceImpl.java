package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.dto.DishDTO;
import com.dingdingcatering.dto.DishPageQueryDTO;
import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.entity.DishFlavor;
import com.dingdingcatering.entity.Flavor;
import com.dingdingcatering.enumeration.CacheType;
import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.exception.DeletionNotAllowedException;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.DishService;
import com.dingdingcatering.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

    @SuppressWarnings("resource")
    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.debug("分页查询菜品: page={}, pageSize={}, name={}, categoryId={}",
                dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize(),
                dishPageQueryDTO.getName(), dishPageQueryDTO.getCategoryId());
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        List<Long> ids = dishMapper.pageQueryIds(dishPageQueryDTO);
        Page<Long> page = (Page<Long>) ids;
        log.debug("分页查询菜品ID完成: total={}", page.getTotal());
        List<DishVO> list = ids.isEmpty() ? Collections.emptyList() : dishMapper.pageQuery(ids);
        return new PageResult<>(page.getTotal(), list);
    }

    @Override
    public DishVO getById(Long id) {
        log.debug("根据ID查询菜品: id={}", id);
        DishVO dishVO = dishMapper.getById(id);
        if (dishVO == null) {
            throw new DeletionNotAllowedException(ErrorCode.DISH_IS_NULL);
        }
        List<Flavor> flavors = dishMapper.queryFlavorByDishId(id);
        if (flavors != null && !flavors.isEmpty()) {
            dishVO.setFlavors(flavors);
        }
        return dishVO;
    }

    @Override
    public List<Dish> getList(Long categoryId) {
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
        addAllDishFlavor(dishDTO, dish.getId());
        log.info("新增菜品成功: id={}", dish.getId());
    }

    private void addAllDishFlavor(DishDTO dishDTO, Long id) {
        if (dishDTO.getFlavors() == null || dishDTO.getFlavors().isEmpty()) {
            return;
        }
        List<DishFlavor> dishFlavors = dishDTO.getFlavors().stream()
                .map(flavor -> DishFlavor.builder()
                        .dishId(id)
                        .flavorId(flavor.getId())
                        .flavorName(flavor.getFlavorName())
                        .flavorValue(flavor.getValueList())
                        .valueList(flavor.getValueList())
                        .build())
                .collect(Collectors.toList());
        if (!dishFlavors.isEmpty()) {
            dishMapper.addDishFlavor(dishFlavors);
        }
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
            throw new DeletionNotAllowedException(ErrorCode.DISH_IS_NULL);
        }
        List<String> nameList = dishMapper.queryStatusByIds(idList);
        if (!nameList.isEmpty()) {
            log.warn("删除菜品失败: 菜品已起售, names={}", nameList);
            throw new DeletionNotAllowedException(ErrorCode.DISH_ON_SALE, String.join("、", nameList));
        }
        List<HashMap<String, String>> setmealList = dishMapper.querySetmealByIds(idList);
        if (!setmealList.isEmpty()) {
            String detail = setmealList.stream()
                    .map(map -> map.get("name") + "：" + map.get("setmeal_names"))
                    .collect(Collectors.joining("、"));
            log.warn("删除菜品失败: 菜品被套餐关联, detail={}", detail);
            throw new DeletionNotAllowedException(ErrorCode.DISH_RELATED_BY_SETMEAL, detail);
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

        dishMapper.deleteFlavorByDishIds(List.of(dishDTO.getId()));
        addAllDishFlavor(dishDTO, dishDTO.getId());
        log.info("更新菜品成功: id={}", dish.getId());
    }

    @Override
    @AutoClearCache(CacheType.DISH)
    public void updateStatus(Integer status, Long id) {
        log.info("更新菜品状态: id={}, status={}", id, status);
        dishMapper.updateStatus(status, id);
    }
}