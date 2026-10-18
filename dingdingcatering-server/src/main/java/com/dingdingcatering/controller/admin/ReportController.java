package com.dingdingcatering.controller.admin;

import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.ReportService;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据统计报表控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 导出运营报表接口。
     * <p>
     * 生成Excel文件并返回给前端下载。
     *
     * @param response HTTP响应对象（用于写入Excel文件流）
     */
    @GetMapping("/export")
    public void exportReport(HttpServletResponse response) {
        reportService.exportReport(response);
    }

    /**
     * 查询销量Top10报表接口。
     *
     * @param begin 开始日期（格式：yyyy-MM-dd）
     * @param end   结束日期（格式：yyyy-MM-dd）
     * @return 销量Top10商品数据
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10Dish(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        SalesTop10ReportVO reportVO = reportService.top10Dish(queryDTO);
        return Result.success(reportVO);
    }

    /**
     * 查询用户统计报表接口。
     *
     * @param begin 开始日期（格式：yyyy-MM-dd）
     * @param end   结束日期（格式：yyyy-MM-dd）
     * @return 用户总量及新增用户数据
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        UserReportVO reportVO = reportService.userStatistics(queryDTO);
        return Result.success(reportVO);
    }

    /**
     * 查询营业额统计报表接口。
     *
     * @param begin 开始日期（格式：yyyy-MM-dd）
     * @param end   结束日期（格式：yyyy-MM-dd）
     * @return 每日营业额数据
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        TurnoverReportVO reportVO = reportService.turnoverStatistics(queryDTO);
        return Result.success(reportVO);
    }

    /**
     * 查询订单统计报表接口。
     *
     * @param begin 开始日期（格式：yyyy-MM-dd）
     * @param end   结束日期（格式：yyyy-MM-dd）
     * @return 订单数、有效订单数及完成率数据
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(@RequestParam String begin, @RequestParam String end) {
        DataOverViewQueryDTO queryDTO = DataOverViewQueryDTO.addTime(begin, end);
        OrderReportVO reportVO = reportService.ordersStatistics(queryDTO);
        return Result.success(reportVO);
    }
}
