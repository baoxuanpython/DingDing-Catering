package com.dingdingcatering.service.admin.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    private final SetMealMapper setmealMapper;
    private final DishMapper dishMapper;

    public SetmealServiceImpl(SetMealMapper setmealMapper, DishMapper dishMapper) {
        this.setmealMapper = setmealMapper;
        this.dishMapper = dishMapper;
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
        setMealDishes.forEach(setMealDish -> setMealDish.setSetmealId(setmeal.getId()));
        setmealMapper.addSetMealDishes(setMealDishes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSetmeal(String ids) {
        List<Long> idList = Arrays.stream(ids.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(Long::parseLong).toList();
        List<String> nameList = setmealMapper.queryStatusByIds(idList);
        if (!nameList.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE + "：" + String.join("、", nameList));
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
        if (status.equals(StatusConstant.ENABLE)) {
            List<Long> dishesId = setmealMapper.queryDishesId(id);
            log.info("dishesId:{}", dishesId);
            List<String> disableDishNames = dishMapper.queryByStatus(dishesId);
            log.info("disabledDishNames:{}", disableDishNames);
            if (disableDishNames.isEmpty()) {
                setmealMapper.updateStatus(status, id);
            } else {
                String dishNames = String.join("、", disableDishNames);
                throw new SetMealEnableFailedException(
                        MessageConstant.SETMEAL_ENABLE_FAILED + "：" + dishNames);
            }
        } else {
            setmealMapper.updateStatus(status, id);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSetmeal(SetMealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetMeal(setmeal);
        setmealMapper.deleteSetMealDishes(List.of(setmeal.getId()));
        List<SetMealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if (setMealDishes == null || setMealDishes.isEmpty()) {
            return;
        }
        setMealDishes.forEach(setMealDish -> setMealDish.setSetmealId(setmeal.getId()));
        log.info("setMealDishes: {}", setMealDishes);
        setmealMapper.addSetMealDishes(setMealDishes);
    }
}
