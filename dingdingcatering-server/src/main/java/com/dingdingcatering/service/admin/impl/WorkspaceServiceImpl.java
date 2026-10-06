package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.mapper.admin.WorkspaceMapper;
import com.dingdingcatering.service.admin.WorkspaceService;
import com.dingdingcatering.vo.BusinessDataVO;
import com.dingdingcatering.vo.DishOverViewVO;
import com.dingdingcatering.vo.OrderOverViewVO;
import com.dingdingcatering.vo.SetMealOverViewVO;
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
