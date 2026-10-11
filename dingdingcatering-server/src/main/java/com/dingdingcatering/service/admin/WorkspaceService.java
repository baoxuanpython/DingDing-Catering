package com.dingdingcatering.service.admin;

import com.dingdingcatering.vo.BusinessDataVO;
import com.dingdingcatering.vo.DishOverViewVO;
import com.dingdingcatering.vo.OrderOverViewVO;
import com.dingdingcatering.vo.SetMealOverViewVO;

/**
 * 工作台数据概览服务接口
 */
public interface WorkspaceService {
    /**
     * 获取今日运营数据。
     *
     * @return 营业额、有效订单数、订单完成率、平均客单价、新增用户数
     */
    BusinessDataVO getBusinessData();

    /**
     * 获取套餐总览。
     *
     * @return 已启售/已停售套餐数量
     */
    SetMealOverViewVO getOverviewSetmeal();

    /**
     * 获取菜品总览。
     *
     * @return 已启售/已停售菜品数量
     */
    DishOverViewVO getOverviewDishes();

    /**
     * 获取订单概览。
     *
     * @return 各状态订单数量统计
     */
    OrderOverViewVO getOverviewOrders();
}
