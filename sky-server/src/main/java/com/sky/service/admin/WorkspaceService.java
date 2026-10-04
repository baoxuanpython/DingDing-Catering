package com.sky.service.admin;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetMealOverViewVO;

public interface WorkspaceService {
    BusinessDataVO getBusinessData();

    SetMealOverViewVO getOverviewSetmeals();

    DishOverViewVO getOverviewDishes();

    OrderOverViewVO getOverviewOrders();
}
