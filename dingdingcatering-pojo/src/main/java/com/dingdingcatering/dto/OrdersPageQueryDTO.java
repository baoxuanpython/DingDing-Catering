package com.dingdingcatering.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单分页查询数据传输对象
 */
@Data
public class OrdersPageQueryDTO implements Serializable {

    /** 页码 */
    private int page;

    /** 每页记录数 */
    private int pageSize;

    /** 订单编号（模糊查询） */
    private String number;

    /** 手机号（模糊查询） */
    private  String phone;

    /** 订单状态筛选 */
    private Integer status;

    /** 开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /** 结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 下单用户ID筛选 */
    private Long userId;

}
