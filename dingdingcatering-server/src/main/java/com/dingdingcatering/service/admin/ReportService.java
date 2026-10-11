package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 数据统计报表服务接口
 */
public interface ReportService {
    /**
     * 查询销量Top10商品。
     *
     * @param queryDTO 时间范围
     * @return 销量Top10数据
     */
    SalesTop10ReportVO top10Dish(DataOverViewQueryDTO queryDTO);

    /**
     * 查询用户统计数据。
     *
     * @param queryDTO 时间范围
     * @return 用户总量及新增用户数据
     */
    UserReportVO userStatistics(DataOverViewQueryDTO queryDTO);

    /**
     * 查询营业额统计数据。
     *
     * @param queryDTO 时间范围
     * @return 每日营业额数据
     */
    TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO queryDTO);

    /**
     * 查询订单统计数据。
     *
     * @param queryDTO 时间范围
     * @return 订单数、有效订单数及完成率
     */
    OrderReportVO ordersStatistics(DataOverViewQueryDTO queryDTO);

    /**
     * 导出运营报表（Excel）。
     *
     * @param response HTTP响应对象
     */
    void exportReport(HttpServletResponse response);
}
