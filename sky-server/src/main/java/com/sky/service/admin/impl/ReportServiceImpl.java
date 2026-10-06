package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.mapper.admin.ReportMapper;
import com.dingdingcatering.service.admin.ReportService;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;
    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    public SalesTop10ReportVO top10Dish(DataOverViewQueryDTO queryDTO) {
        return reportMapper.top10Dish(queryDTO);
    }

    @Override
    public UserReportVO userStatistics(DataOverViewQueryDTO queryDTO) {
        return reportMapper.userStatistics(queryDTO);
    }

    @Override
    public TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO queryDTO) {
        return reportMapper.turnoverStatistics(queryDTO);
    }

    @Override
    public OrderReportVO ordersStatistics(DataOverViewQueryDTO queryDTO) {
        return reportMapper.ordersStatistics(queryDTO);
    }
}
