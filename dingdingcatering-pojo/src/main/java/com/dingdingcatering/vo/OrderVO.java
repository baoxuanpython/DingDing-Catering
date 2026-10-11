package com.dingdingcatering.vo;

import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 订单详情视图对象（扩展Orders实体）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    /** 订单菜品信息（字符串展示格式） */
    private String orderDishes;

    /** 订单详情列表 */
    private List<OrderDetail> orderDetailList;

}
