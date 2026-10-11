package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.dto.DailyStatisticsDTO;
import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.dto.DishSalesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper  {
    List<DishSalesDTO> top10Dish(DataOverViewQueryDTO queryDTO);

    List<DailyStatisticsDTO> userStatistics(DataOverViewQueryDTO queryDTO);

    List<DailyStatisticsDTO> turnoverStatistics(DataOverViewQueryDTO queryDTO);

    List<DailyStatisticsDTO> ordersStatistics(DataOverViewQueryDTO queryDTO);
}
