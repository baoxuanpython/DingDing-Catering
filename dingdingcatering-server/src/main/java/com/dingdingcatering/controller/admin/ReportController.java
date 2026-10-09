package com.dingdingcatering.controller.admin;

import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.ReportService;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/admin/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/export")
    public Result<Void> exportReport(HttpServletRequest request) {
//        TODO 导出订单报表
        return Result.success();
    }

    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10Dish(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        SalesTop10ReportVO reportVO = reportService.top10Dish(queryDTO);
        return Result.success(reportVO);
    }

    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        UserReportVO reportVO = reportService.userStatistics(queryDTO);
        return Result.success(reportVO);
    }

    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        TurnoverReportVO reportVO = reportService.turnoverStatistics(queryDTO);
        return Result.success(reportVO);
    }

    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        OrderReportVO reportVO = reportService.ordersStatistics(queryDTO);
        return Result.success(reportVO);
    }
}
