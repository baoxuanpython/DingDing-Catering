package com.sky.service.admin;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

public interface ReportService {
    SalesTop10ReportVO top10Dish(DataOverViewQueryDTO queryDTO);

    UserReportVO userStatistics(DataOverViewQueryDTO queryDTO);

    TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO queryDTO);

    OrderReportVO ordersStatistics(DataOverViewQueryDTO queryDTO);
}
