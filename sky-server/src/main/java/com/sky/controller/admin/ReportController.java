package com.sky.controller.admin;

import com.sky.annotation.AutoLogDTO;
import com.sky.dto.DataOverViewQueryDTO;
import com.sky.result.Result;
import com.sky.service.admin.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
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
    @AutoLogDTO("导出订单报表")
    public Result<Void> exportReport(HttpServletRequest request) {
//        TODO 导出订单报表
        return Result.success();
    }
    @GetMapping("/top10")
    @AutoLogDTO("查询销量Top10菜品")
    public Result<SalesTop10ReportVO> top10Dish(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        SalesTop10ReportVO reportVO = reportService.top10Dish(queryDTO);
        return Result.success(reportVO);
    }
    @GetMapping("/userStatistics")
    @AutoLogDTO("查询用户统计信息")
    public Result<UserReportVO> userStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        UserReportVO reportVO = reportService.userStatistics(queryDTO);
        return Result.success(reportVO);
    }
    @GetMapping("/turnoverStatistics")
    @AutoLogDTO("查询营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        TurnoverReportVO reportVO = reportService.turnoverStatistics(queryDTO);
        return Result.success(reportVO);
    }
    @GetMapping("/ordersStatistics")
    @AutoLogDTO(value = "查询订单统计",returnResult = true)
    public Result<OrderReportVO> ordersStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        OrderReportVO reportVO = reportService.ordersStatistics(queryDTO);
        return Result.success(reportVO);
    }
}
