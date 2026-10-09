package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.vo.BusinessDataVO;
import com.dingdingcatering.vo.DishOverViewVO;
import com.dingdingcatering.vo.OrderOverViewVO;
import com.dingdingcatering.vo.SetMealOverViewVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkspaceMapper  {

    BusinessDataVO getBusinessData();

    SetMealOverViewVO getOverviewSetmeal();

    DishOverViewVO getOverviewDishes();

    OrderOverViewVO getOverviewOrders();
}
