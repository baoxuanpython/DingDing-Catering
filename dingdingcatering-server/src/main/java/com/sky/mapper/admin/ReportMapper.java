package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper  {
    SalesTop10ReportVO top10Dish(DataOverViewQueryDTO queryDTO);

    UserReportVO userStatistics(DataOverViewQueryDTO queryDTO);

    TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO queryDTO);

    OrderReportVO ordersStatistics(DataOverViewQueryDTO queryDTO);
}
