package com.dingdingcatering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据概览查询数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataOverViewQueryDTO implements Serializable {

    /** 开始时间 */
    private LocalDateTime begin;

    /** 结束时间 */
    private LocalDateTime end;

    /**
     * 构建查询时间范围（自动补充起止时间的时分秒）
     * @param begin 开始日期（格式：yyyy-MM-dd）
     * @param end 结束日期（格式：yyyy-MM-dd）
     * @return 查询对象（begin为当天00:00:00，end为当天23:59:59）
     */
    public static DataOverViewQueryDTO addTime(String begin, String end){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginTime = LocalDateTime.parse(begin + " 00:00:00", formatter);
        LocalDateTime endTime = LocalDateTime.parse(end + " 23:59:59", formatter);
        return DataOverViewQueryDTO.builder()
                .begin(beginTime)
                .end(endTime)
                .build();
    }
}
