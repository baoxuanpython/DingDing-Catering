package com.sky.mapper.admin;

import com.sky.annotation.AutoLogDTO;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetMealOverViewVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.GetMapping;

@Mapper
public interface WorkspaceMapper  {

    BusinessDataVO getBusinessData();

    SetMealOverViewVO getOverviewSetmeals();

    DishOverViewVO getOverviewDishes();

    OrderOverViewVO getOverviewOrders();
}
