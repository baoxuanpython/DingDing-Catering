package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.constant.StatusConstant;
import com.dingdingcatering.dto.DishFlavorUpdateDTO;
import com.dingdingcatering.dto.SetMealDTO;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.entity.SetMealDish;
import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.enumeration.CacheType;
import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.exception.DeletionNotAllowedException;
import com.dingdingcatering.exception.SetMealEnableFailedException;
import com.dingdingcatering.exception.SetMealNoDishesException;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.mapper.admin.SetMealMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.SetmealService;
import com.dingdingcatering.vo.SetMealVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("adminSetMealServiceImpl")
public class SetMealServiceImpl implements SetmealService {
    private final SetMealMapper setmealMapper;
    private final DishMapper dishMapper;

    public SetMealServiceImpl(@Qualifier("adminSetMealMapper") SetMealMapper setmealMapper, DishMapper dishMapper) {
        this.setmealMapper = setmealMapper;
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult<SetMealVO> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO) {
        log.debug("分页查询套餐: page={}, pageSize={}, name={}",
                setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize(), setmealPageQueryDTO.getName());
        try (Page<SetMealVO> page = PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize())) {
            List<SetMealVO> setmealList = setmealMapper.pageQueryBasic(setmealPageQueryDTO);
            log.debug("分页查询套餐完成: total={}", page.getTotal());

            if (setmealList.isEmpty()) {
                return new PageResult<>(page.getTotal(), setmealList);
            }

            List<Long> setmealIds = setmealList.stream()
                    .map(SetMealVO::getId)
                    .collect(Collectors.toList());

            List<SetMealDish> allDishes = setmealMapper.queryDishesBySetmealIds(setmealIds);
            Map<Long, List<SetMealDish>> dishesMap = allDishes.stream()
                    .collect(Collectors.groupingBy(SetMealDish::getSetmealId));

            for (SetMealVO setmealVO : setmealList) {
                List<SetMealDish> dishes = dishesMap.getOrDefault(setmealVO.getId(), List.of());
                if (dishes.isEmpty()) {
                    log.warn("套餐无菜品: setmealId ={}", setmealVO.getId());
                    throw new SetMealNoDishesException(ErrorCode.SETMEAL_NO_DISHES, "套餐[" + setmealVO.getName() + "]未关联任何菜品");
                }
                setmealVO.setSetmealDishes(dishes);
            }

            return new PageResult<>(page.getTotal(), setmealList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AutoClearCache({CacheType.SETMEAL_CATEGORY, CacheType.SETMEAL_DISH})
    public void addSetmeal(SetMealDTO setmealDTO) {
        log.info("新增套餐: name={}, categoryId={}", setmealDTO.getName(), setmealDTO.getCategoryId());
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.addSetMeal(setmeal);
        List<SetMealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if (setMealDishes == null || setMealDishes.isEmpty()) {
            log.warn("新增套餐: 套餐菜品为空, setmealId={}", setmeal.getId());
            return;
        }
        setMealDishes.forEach(setMealDish -> setMealDish.setSetmealId(setmeal.getId()));
        setmealMapper.addSetMealDishes(setMealDishes);
        batchUpdateDishFlavors(setMealDishes);
        log.info("新增套餐成功: id={}, dishCount={}", setmeal.getId(), setMealDishes.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AutoClearCache({CacheType.SETMEAL_CATEGORY, CacheType.SETMEAL_DISH})
    public void deleteSetmeal(String ids) {
        log.info("删除套餐: ids={}", ids);
        List<Long> idList = Arrays.stream(ids.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(Long::parseLong).toList();
        List<String> nameList = setmealMapper.queryStatusByIds(idList);
        if (!nameList.isEmpty()) {
            log.warn("删除套餐失败: 套餐已起售, names={}", nameList);
            throw new DeletionNotAllowedException(ErrorCode.SETMEAL_ON_SALE, String.join("、", nameList));
        }
        setmealMapper.deleteSetMeal(idList);
        setmealMapper.deleteSetMealDishes(idList);
        log.info("删除套餐成功: idCount={}", idList.size());
    }

    @Override
    public SetMealVO getSetmealById(Long id) {
        log.debug("查询套餐详情: id={}", id);
        SetMealVO setmealVO = setmealMapper.getSetMealById(id);
        if (setmealVO == null) {
            log.warn("查询套餐详情失败: 套餐不存在, id={}", id);
            throw new SetMealNoDishesException(ErrorCode.SETMEAL_IS_NULL, "套餐不存在");
        }
        List<SetMealDish> dishes = setmealVO.getSetmealDishes();
        if (dishes == null || dishes.isEmpty()) {
            log.warn("套餐无菜品: setmealId={}", id);
            throw new SetMealNoDishesException(ErrorCode.SETMEAL_NO_DISHES, "套餐[" + setmealVO.getName() + "]未关联任何菜品");
        }
        return setmealVO;
    }

    @Override
    @AutoClearCache({CacheType.SETMEAL_CATEGORY, CacheType.SETMEAL_DISH})
    public void updateStatus(Integer status, Long id) {
        log.info("更新套餐状态: id={}, status={}", id, status);
        if (status.equals(StatusConstant.ENABLE)) {
            List<Long> dishesId = setmealMapper.queryDishesId(id);
            log.debug("套餐关联菜品ID: {}", dishesId);
            List<String> disableDishNames = dishMapper.queryByStatus(dishesId);
            log.debug("套餐中停售菜品: {}", disableDishNames);
            if (disableDishNames.isEmpty()) {
                setmealMapper.updateStatus(status, id);
                log.info("更新套餐状态成功: id={}, status=起售", id);
            } else {
                String dishNames = String.join("、", disableDishNames);
                log.warn("更新套餐状态失败: 包含停售菜品, id={}, dishNames={}", id, dishNames);
                throw new SetMealEnableFailedException(ErrorCode.SETMEAL_ENABLE_FAILED, dishNames);
            }
        } else {
            setmealMapper.updateStatus(status, id);
            log.info("更新套餐状态成功: id={}, status=停售", id);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AutoClearCache({CacheType.SETMEAL_CATEGORY, CacheType.SETMEAL_DISH})
    public void updateSetmeal(SetMealDTO setmealDTO) {
        log.info("更新套餐: id={}, name={}", setmealDTO.getId(), setmealDTO.getName());
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetMeal(setmeal);
        setmealMapper.deleteSetMealDishes(List.of(setmeal.getId()));
        List<SetMealDish> setMealDishes = setmealDTO.getSetmealDishes();
        setMealDishes.forEach(setMealDish -> setMealDish.setSetmealId(setmeal.getId()));
        log.debug("更新套餐菜品: setmealId={}, dishCount={}", setmeal.getId(), setMealDishes.size());
        setmealMapper.addSetMealDishes(setMealDishes);
        batchUpdateDishFlavors(setMealDishes);
        log.info("更新套餐成功: id={}, dishCount={}", setmeal.getId(), setMealDishes.size());
    }

    private void batchUpdateDishFlavors(List<SetMealDish> setMealDishes) {
        List<DishFlavorUpdateDTO> dishFlavorUpdates = setMealDishes.stream()
                .map(dish -> DishFlavorUpdateDTO.builder()
                        .dishId(dish.getDishId())
                        .flavors(dish.getFlavors() == null || dish.getFlavors().isEmpty()
                                ? ""
                                : dish.getFlavors().stream().map(String::valueOf).collect(Collectors.joining(",")))
                        .build())
                .collect(Collectors.toList());
        log.debug("批量更新菜品口味: dishCount={}", dishFlavorUpdates.size());
        setmealMapper.batchUpdateDishFlavors(dishFlavorUpdates);
    }
}
