package com.sky.service.admin.impl;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.mapper.admin.ReportMapper;
import com.sky.service.admin.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
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
