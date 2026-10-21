package com.dingdingcatering.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户报表视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportVO implements Serializable {

    /** 日期列表，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03。同天查询时此字段为 null */
    private String dateList;

    /** 小时列表（同天查询时使用），以逗号分隔，例如：0,1,2,...,23。跨天查询时此字段为 null */
    private String hourList;

    /** 用户总量列表，以逗号分隔，例如：200,210,220 */
    private String totalUserList;

    /** 新增用户列表，以逗号分隔，例如：20,21,10 */
    private String newUserList;

}