package com.dingdingcatering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 每日统计数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatisticsDTO implements Serializable {

    /** 统计日期 */
    private LocalDate date;

    /** 当日新增用户数 */
    private Long dailyNew;

    /** 用户总量 */
    private Long totalUsers;

    /** 当日营业额 */
    private BigDecimal dailyTurnover;

    /** 订单总数 */
    private Long totalCnt;

    /** 有效订单数 */
    private Long validCnt;

}
