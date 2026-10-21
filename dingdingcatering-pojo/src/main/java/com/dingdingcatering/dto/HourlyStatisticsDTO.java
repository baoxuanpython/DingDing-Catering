package com.dingdingcatering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 小时级统计数据传输对象（同天查询时使用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyStatisticsDTO implements Serializable {

    /** 小时（0~23） */
    private Integer hour;

    /** 该小时新增用户数 */
    private Long dailyNew;

    /** 截至该小时的累计用户总数 */
    private Long totalUsers;

    /** 该小时营业额 */
    private BigDecimal dailyTurnover;

    /** 该小时订单总数 */
    private Long totalCnt;

    /** 该小时有效订单数 */
    private Long validCnt;

}