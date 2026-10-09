package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.enumeration.CacheType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.constant.StatusConstant;
import com.dingdingcatering.dto.SetMealDTO;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.entity.SetMealDish;
import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.exception.DeletionNotAllowedException;
import com.dingdingcatering.exception.SetMealEnableFailedException;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.mapper.admin.SetMealMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.SetmealService;
import com.dingdingcatering.vo.SetMealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
            List<SetMealVO> list = setmealMapper.pageQuery(setmealPageQueryDTO);
            log.debug("分页查询套餐完成: total={}", page.getTotal());
            return new PageResult<>(page.getTotal(), list);
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
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE + "：" + String.join("、", nameList));
        }
        setmealMapper.deleteSetMeal(idList);
        setmealMapper.deleteSetMealDishes(idList);
        log.info("删除套餐成功: idCount={}", idList.size());
    }

    @Override
    public SetMealVO getSetmealById(Long id) {
        log.debug("查询套餐详情: id={}", id);
        return setmealMapper.getSetMealById(id);
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
                throw new SetMealEnableFailedException(
                        MessageConstant.SETMEAL_ENABLE_FAILED + "：" + dishNames);
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
        if (setMealDishes == null || setMealDishes.isEmpty()) {
            log.info("更新套餐成功: id={}, dishCount=0", setmeal.getId());
            return;
        }
        setMealDishes.forEach(setMealDish -> setMealDish.setSetmealId(setmeal.getId()));
        log.debug("更新套餐菜品: setmealId={}, dishCount={}", setmeal.getId(), setMealDishes.size());
        setmealMapper.addSetMealDishes(setMealDishes);
        log.info("更新套餐成功: id={}, dishCount={}", setmeal.getId(), setMealDishes.size());
    }
}
