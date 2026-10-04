package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataOverViewQueryDTO implements Serializable {

    private LocalDateTime begin;

    private LocalDateTime end;

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
