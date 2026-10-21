package com.dingdingcatering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
     * @throws IllegalArgumentException 如果日期格式不正确
     */
    public static DataOverViewQueryDTO addTime(String begin, String end) {
        LocalDate beginDate = parseDate(begin, "开始日期");
        LocalDate endDate = parseDate(end, "结束日期");
        return DataOverViewQueryDTO.builder()
                .begin(beginDate.atStartOfDay())
                .end(endDate.atTime(23, 59, 59))
                .build();
    }

    /**
     * 判断查询范围是否为同一天（用于决定返回按天汇总还是按小时明细）。
     * <p>
     * MyBatis 动态 SQL 可通过 {@code <if test="sameDay">} 调用此方法。
     *
     * @return true 如果 begin 和 end 在同一天
     */
    public boolean isSameDay() {
        return begin != null && end != null
                && begin.toLocalDate().equals(end.toLocalDate());
    }

    private static LocalDate parseDate(String dateStr, String fieldName) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }
        try {
            return LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    fieldName + "格式错误，期望格式：yyyy-MM-dd，实际值：" + dateStr, e);
        }
    }
}