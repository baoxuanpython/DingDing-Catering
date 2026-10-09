package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.mapper.admin.ReportMapper;
import com.dingdingcatering.service.admin.ReportService;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;
    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    public SalesTop10ReportVO top10Dish(DataOverViewQueryDTO queryDTO) {
        log.debug("查询销量Top10菜品: begin={}, end={}", queryDTO.getBegin(), queryDTO.getEnd());
        return reportMapper.top10Dish(queryDTO);
    }

    @Override
    public UserReportVO userStatistics(DataOverViewQueryDTO queryDTO) {
        log.debug("查询用户统计: begin={}, end={}", queryDTO.getBegin(), queryDTO.getEnd());
        return reportMapper.userStatistics(queryDTO);
    }

    @Override
    public TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO queryDTO) {
        log.debug("查询营业额统计: begin={}, end={}", queryDTO.getBegin(), queryDTO.getEnd());
        return reportMapper.turnoverStatistics(queryDTO);
    }

    @Override
    public OrderReportVO ordersStatistics(DataOverViewQueryDTO queryDTO) {
        log.debug("查询订单统计: begin={}, end={}", queryDTO.getBegin(), queryDTO.getEnd());
        return reportMapper.ordersStatistics(queryDTO);
    }
}
