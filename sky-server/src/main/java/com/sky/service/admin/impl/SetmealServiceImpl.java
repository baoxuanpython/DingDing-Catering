package com.sky.service.admin.impl;

import com.sky.mapper.admin.SetmealMapper;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl implements com.sky.service.admin.impl.SetmealService {
    private final SetmealMapper setmealMapper;
    public SetmealServiceImpl(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }
}
