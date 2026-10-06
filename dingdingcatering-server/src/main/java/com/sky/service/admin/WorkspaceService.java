package com.dingdingcatering.service.admin;

import com.dingdingcatering.vo.BusinessDataVO;
import com.dingdingcatering.vo.DishOverViewVO;
import com.dingdingcatering.vo.OrderOverViewVO;
import com.dingdingcatering.vo.SetMealOverViewVO;

public interface WorkspaceService {
    BusinessDataVO getBusinessData();

    SetMealOverViewVO getOverviewSetmeals();

    DishOverViewVO getOverviewDishes();

    OrderOverViewVO getOverviewOrders();
}
