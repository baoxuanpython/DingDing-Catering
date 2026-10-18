package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.dto.DailyStatisticsDTO;
import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.dto.DishSalesDTO;
import com.dingdingcatering.mapper.admin.ReportMapper;
import com.dingdingcatering.service.admin.ReportService;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
        List<DishSalesDTO> dishSalesList = reportMapper.top10Dish(queryDTO);
        log.debug("top10Dish详细信息: {}", dishSalesList);
        
        String nameList = dishSalesList.stream()
                .map(DishSalesDTO::getName)
                .collect(Collectors.joining(","));
        String numberList = dishSalesList.stream()
                .map(dish -> dish.getSales().toString())
                .collect(Collectors.joining(","));
        
        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    @Override
    public UserReportVO userStatistics(DataOverViewQueryDTO queryDTO) {
        log.debug("查询用户统计: begin={}, end={}", queryDTO.getBegin(), queryDTO.getEnd());
        List<DailyStatisticsDTO> statisticsList = reportMapper.userStatistics(queryDTO);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateList = statisticsList.stream()
                .map(stat -> stat.getDate().format(formatter))
                .collect(Collectors.joining(","));
        String newUserList = statisticsList.stream()
                .map(stat -> stat.getDailyNew().toString())
                .collect(Collectors.joining(","));
        String totalUserList = statisticsList.stream()
                .map(stat -> stat.getTotalUsers().toString())
                .collect(Collectors.joining(","));
        
        return UserReportVO.builder()
                .dateList(dateList)
                .newUserList(newUserList)
                .totalUserList(totalUserList)
                .build();
    }

    @Override
    public TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO queryDTO) {
        log.debug("查询营业额统计: begin={}, end={}", queryDTO.getBegin(), queryDTO.getEnd());
        List<DailyStatisticsDTO> statisticsList = reportMapper.turnoverStatistics(queryDTO);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateList = statisticsList.stream()
                .map(stat -> stat.getDate().format(formatter))
                .collect(Collectors.joining(","));
        String turnoverList = statisticsList.stream()
                .map(stat -> stat.getDailyTurnover().toString())
                .collect(Collectors.joining(","));
        
        return TurnoverReportVO.builder()
                .dateList(dateList)
                .turnoverList(turnoverList)
                .build();
    }

    @Override
    public OrderReportVO ordersStatistics(DataOverViewQueryDTO queryDTO) {
        log.debug("查询订单统计: begin={}, end={}", queryDTO.getBegin(), queryDTO.getEnd());
        List<DailyStatisticsDTO> statisticsList = reportMapper.ordersStatistics(queryDTO);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateList = statisticsList.stream()
                .map(stat -> stat.getDate().format(formatter))
                .collect(Collectors.joining(","));
        String orderCountList = statisticsList.stream()
                .map(stat -> stat.getTotalCnt().toString())
                .collect(Collectors.joining(","));
        String validOrderCountList = statisticsList.stream()
                .map(stat -> stat.getValidCnt().toString())
                .collect(Collectors.joining(","));
        
        long totalOrderCount = statisticsList.stream()
                .mapToLong(DailyStatisticsDTO::getTotalCnt)
                .sum();
        long validOrderCount = statisticsList.stream()
                .mapToLong(DailyStatisticsDTO::getValidCnt)
                .sum();
        double orderCompletionRate = totalOrderCount == 0 ? 0 : 
                Math.round((double) validOrderCount / totalOrderCount * 100.0) / 100.0;
        
        return OrderReportVO.builder()
                .dateList(dateList)
                .orderCountList(orderCountList)
                .validOrderCountList(validOrderCountList)
                .totalOrderCount((int) totalOrderCount)
                .validOrderCount((int) validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public void exportReport(HttpServletResponse response) {
        log.debug("导出报表");
    }
}