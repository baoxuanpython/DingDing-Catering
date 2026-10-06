package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;

public interface ReportService {
    SalesTop10ReportVO top10Dish(DataOverViewQueryDTO queryDTO);

    UserReportVO userStatistics(DataOverViewQueryDTO queryDTO);

    TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO queryDTO);

    OrderReportVO ordersStatistics(DataOverViewQueryDTO queryDTO);
}
