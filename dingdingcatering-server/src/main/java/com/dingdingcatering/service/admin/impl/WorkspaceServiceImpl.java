package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.mapper.admin.WorkspaceMapper;
import com.dingdingcatering.service.admin.WorkspaceService;
import com.dingdingcatering.vo.BusinessDataVO;
import com.dingdingcatering.vo.DishOverViewVO;
import com.dingdingcatering.vo.OrderOverViewVO;
import com.dingdingcatering.vo.SetMealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceMapper workspaceMapper;
    public WorkspaceServiceImpl(WorkspaceMapper workspaceMapper) {
        this.workspaceMapper = workspaceMapper;
    }

    @Override
    public BusinessDataVO getBusinessData() {
        log.debug("查询工作台业务数据");
        return workspaceMapper.getBusinessData();
    }

    @Override
    public SetMealOverViewVO getOverviewSetmeal() {
        log.debug("查询工作台套餐概览");
        return workspaceMapper.getOverviewSetmeal();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        log.debug("查询工作台菜品概览");
        return workspaceMapper.getOverviewDishes();
    }

    @Override
    public OrderOverViewVO getOverviewOrders() {
        log.debug("查询工作台订单概览");
        return workspaceMapper.getOverviewOrders();
    }
}
