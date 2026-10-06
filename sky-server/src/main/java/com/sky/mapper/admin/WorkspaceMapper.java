package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.annotation.AutoLogDTO;
import com.dingdingcatering.vo.BusinessDataVO;
import com.dingdingcatering.vo.DishOverViewVO;
import com.dingdingcatering.vo.OrderOverViewVO;
import com.dingdingcatering.vo.SetMealOverViewVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.GetMapping;

@Mapper
public interface WorkspaceMapper  {

    BusinessDataVO getBusinessData();

    SetMealOverViewVO getOverviewSetmeals();

    DishOverViewVO getOverviewDishes();

    OrderOverViewVO getOverviewOrders();
}
