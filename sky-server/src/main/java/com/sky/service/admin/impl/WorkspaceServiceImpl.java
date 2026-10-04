package com.sky.service.admin.impl;

import com.sky.mapper.admin.WorkspaceMapper;
import com.sky.service.admin.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetMealOverViewVO;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceMapper workspaceMapper;
    public WorkspaceServiceImpl(WorkspaceMapper workspaceMapper) {
        this.workspaceMapper = workspaceMapper;
    }

    @Override
    public BusinessDataVO getBusinessData() {
        return workspaceMapper.getBusinessData();
    }

    @Override
    public SetMealOverViewVO getOverviewSetmeals() {
        return workspaceMapper.getOverviewSetmeals();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        return workspaceMapper.getOverviewDishes();
    }

    @Override
    public OrderOverViewVO getOverviewOrders() {
        return workspaceMapper.getOverviewOrders();
    }
}
