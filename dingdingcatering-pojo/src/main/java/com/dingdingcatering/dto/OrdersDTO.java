package com.dingdingcatering.dto;

import com.dingdingcatering.entity.OrderDetail;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
public class OrdersDTO implements Serializable {

    /** 订单主键 */
    private Long id;

    /** 订单编号 */
    private String number;

    /** 订单状态：1-待付款，2-待派送，3-已派送，4-已完成，5-已取消 */
    private Integer status;

    /** 下单用户ID */
    private Long userId;

    /** 地址簿ID */
    private Long addressBookId;

    /** 下单时间 */
    private LocalDateTime orderTime;

    /** 结账时间 */
    private LocalDateTime checkoutTime;

    /** 支付方式：1-微信支付，2-支付宝支付 */
    private Integer payMethod;

    /** 实收金额 */
    private BigDecimal amount;

    /** 备注信息 */
    private String remark;

    /** 用户名 */
    private String userName;

    /** 手机号 */
    private String phone;

    /** 地址 */
    private String address;

    /** 收货人 */
    private String consignee;

    /** 订单详情列表 */
    private List<OrderDetail> orderDetails;

}
