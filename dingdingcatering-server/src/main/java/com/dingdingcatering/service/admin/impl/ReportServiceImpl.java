package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.dto.DailyStatisticsDTO;
import com.dingdingcatering.dto.DataOverViewQueryDTO;
import com.dingdingcatering.dto.DishSalesDTO;
import com.dingdingcatering.dto.HourlyStatisticsDTO;
import com.dingdingcatering.mapper.admin.ReportMapper;
import com.dingdingcatering.service.admin.ReportService;
import com.dingdingcatering.vo.OrderReportVO;
import com.dingdingcatering.vo.SalesTop10ReportVO;
import com.dingdingcatering.vo.TurnoverReportVO;
import com.dingdingcatering.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        log.debug("查询用户统计: begin={}, end={}, sameDay={}", queryDTO.getBegin(), queryDTO.getEnd(), queryDTO.isSameDay());

        if (queryDTO.isSameDay()) {
            return buildHourlyUserReport(queryDTO);
        }
        return buildDailyUserReport(queryDTO);
    }

    private UserReportVO buildHourlyUserReport(DataOverViewQueryDTO queryDTO) {
        List<HourlyStatisticsDTO> statisticsList = reportMapper.userStatisticsHourly(queryDTO);

        String hourList = statisticsList.stream()
                .map(stat -> stat.getHour().toString())
                .collect(Collectors.joining(","));
        String newUserList = statisticsList.stream()
                .map(stat -> stat.getDailyNew().toString())
                .collect(Collectors.joining(","));
        String totalUserList = statisticsList.stream()
                .map(stat -> stat.getTotalUsers().toString())
                .collect(Collectors.joining(","));

        return UserReportVO.builder()
                .hourList(hourList)
                .newUserList(newUserList)
                .totalUserList(totalUserList)
                .build();
    }

    private UserReportVO buildDailyUserReport(DataOverViewQueryDTO queryDTO) {
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
        log.debug("查询营业额统计: begin={}, end={}, sameDay={}", queryDTO.getBegin(), queryDTO.getEnd(),
                queryDTO.isSameDay());

        if (queryDTO.isSameDay()) {
            return buildHourlyTurnoverReport(queryDTO);
        }
        return buildDailyTurnoverReport(queryDTO);
    }

    private TurnoverReportVO buildHourlyTurnoverReport(DataOverViewQueryDTO queryDTO) {
        List<HourlyStatisticsDTO> statisticsList = reportMapper.turnoverStatisticsHourly(queryDTO);

        String hourList = statisticsList.stream()
                .map(stat -> stat.getHour().toString())
                .collect(Collectors.joining(","));
        String turnoverList = statisticsList.stream()
                .map(stat -> stat.getDailyTurnover().toString())
                .collect(Collectors.joining(","));

        return TurnoverReportVO.builder()
                .hourList(hourList)
                .turnoverList(turnoverList)
                .build();
    }

    private TurnoverReportVO buildDailyTurnoverReport(DataOverViewQueryDTO queryDTO) {
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
        log.debug("查询订单统计: begin={}, end={}, sameDay={}", queryDTO.getBegin(), queryDTO.getEnd(), queryDTO.isSameDay());

        if (queryDTO.isSameDay()) {
            return buildHourlyOrderReport(queryDTO);
        }
        return buildDailyOrderReport(queryDTO);
    }

    private OrderReportVO buildHourlyOrderReport(DataOverViewQueryDTO queryDTO) {
        List<HourlyStatisticsDTO> statisticsList = reportMapper.ordersStatisticsHourly(queryDTO);

        String hourList = statisticsList.stream()
                .map(stat -> stat.getHour().toString())
                .collect(Collectors.joining(","));
        String orderCountList = statisticsList.stream()
                .map(stat -> stat.getTotalCnt().toString())
                .collect(Collectors.joining(","));
        String validOrderCountList = statisticsList.stream()
                .map(stat -> stat.getValidCnt().toString())
                .collect(Collectors.joining(","));

        long totalOrderCount = statisticsList.stream()
                .mapToLong(HourlyStatisticsDTO::getTotalCnt)
                .sum();
        long validOrderCount = statisticsList.stream()
                .mapToLong(HourlyStatisticsDTO::getValidCnt)
                .sum();
        double orderCompletionRate = totalOrderCount == 0 ? 0
                : Math.round((double) validOrderCount / totalOrderCount * 100.0) / 100.0;

        return OrderReportVO.builder()
                .hourList(hourList)
                .orderCountList(orderCountList)
                .validOrderCountList(validOrderCountList)
                .totalOrderCount((int) totalOrderCount)
                .validOrderCount((int) validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    private OrderReportVO buildDailyOrderReport(DataOverViewQueryDTO queryDTO) {
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
        double orderCompletionRate = totalOrderCount == 0 ? 0
                : Math.round((double) validOrderCount / totalOrderCount * 100.0) / 100.0;

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
    public void exportReport(DataOverViewQueryDTO queryDTO, HttpServletResponse response) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String encodedFileName = URLEncoder.encode("运营报表.xlsx", StandardCharsets.UTF_8).replace("+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.setHeader("Cache-Control", "no-cache");

            XSSFCellStyle headerStyle = createHeaderStyle(workbook);
            XSSFCellStyle dataStyle = createDataStyle(workbook);
            XSSFCellStyle amountStyle = createAmountStyle(workbook);
            XSSFCellStyle titleStyle = createTitleStyle(workbook);

            XSSFSheet sheet = workbook.createSheet("运营报表");
            int rowIndex = 0;

            List<HourlyStatisticsDTO> turnoverHourly = null;
            List<HourlyStatisticsDTO> orderHourly = null;
            List<HourlyStatisticsDTO> userHourly = null;
            List<DailyStatisticsDTO> turnoverDaily = null;
            List<DailyStatisticsDTO> orderDaily = null;
            List<DailyStatisticsDTO> userDaily = null;

            if (queryDTO.isSameDay()) {
                turnoverHourly = reportMapper.turnoverStatisticsHourly(queryDTO);
                orderHourly = reportMapper.ordersStatisticsHourly(queryDTO);
                userHourly = reportMapper.userStatisticsHourly(queryDTO);
            } else {
                turnoverDaily = reportMapper.turnoverStatistics(queryDTO);
                orderDaily = reportMapper.ordersStatistics(queryDTO);
                userDaily = reportMapper.userStatistics(queryDTO);
            }
            List<DishSalesDTO> top10 = reportMapper.top10Dish(queryDTO);

            rowIndex = writeTurnoverBlock(sheet, rowIndex, turnoverDaily, turnoverHourly, titleStyle, headerStyle,
                    dataStyle, amountStyle);
            rowIndex = writeOrderBlock(sheet, rowIndex, orderDaily, orderHourly, titleStyle, headerStyle, dataStyle);
            rowIndex = writeUserBlock(sheet, rowIndex, userDaily, userHourly, titleStyle, headerStyle, dataStyle);
            writeTop10Block(sheet, rowIndex, top10, titleStyle, headerStyle, dataStyle);

            autoSizeColumns(sheet);

            workbook.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            log.error("导出报表失败", e);
        }
    }

    private int addBlockTitle(XSSFSheet sheet, int rowIndex, String title, XSSFCellStyle titleStyle, int colCount) {
        XSSFRow titleRow = sheet.createRow(rowIndex);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, colCount - 1));
        titleRow.createCell(0).setCellValue(title);
        for (int i = 0; i < colCount; i++) {
            titleRow.createCell(i).setCellStyle(titleStyle);
        }
        return rowIndex + 2;
    }

    private int writeTurnoverBlock(XSSFSheet sheet, int rowIndex,
            List<DailyStatisticsDTO> dailyData, @NonNull List<HourlyStatisticsDTO> hourlyData,
            XSSFCellStyle titleStyle, XSSFCellStyle headerStyle,
            XSSFCellStyle dataStyle, XSSFCellStyle amountStyle) {
        rowIndex = addBlockTitle(sheet, rowIndex, "营业额统计", titleStyle, 2);

        XSSFRow headerRow = sheet.createRow(rowIndex++);
        boolean isHourly = hourlyData != null;
        headerRow.createCell(0).setCellValue(isHourly ? "小时" : "日期");
        headerRow.createCell(1).setCellValue("营业额（元）");
        for (int i = 0; i < 2; i++)
            headerRow.getCell(i).setCellStyle(headerStyle);

        if (isHourly) {
            for (HourlyStatisticsDTO dto : hourlyData) {
                XSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(dto.getHour() + ":00");
                row.createCell(1).setCellValue(dto.getDailyTurnover().doubleValue());
                row.getCell(1).setCellStyle(amountStyle);
            }
        } else {
            for (DailyStatisticsDTO dto : dailyData) {
                XSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(dto.getDate().toString());
                row.createCell(1).setCellValue(dto.getDailyTurnover().doubleValue());
                row.getCell(1).setCellStyle(amountStyle);
            }
        }
        return rowIndex;
    }

    private int writeOrderBlock(XSSFSheet sheet, int rowIndex,
            List<DailyStatisticsDTO> dailyData, List<HourlyStatisticsDTO> hourlyData,
            XSSFCellStyle titleStyle, XSSFCellStyle headerStyle, XSSFCellStyle dataStyle) {
        rowIndex = addBlockTitle(sheet, rowIndex, "订单统计", titleStyle, 4);

        XSSFRow headerRow = sheet.createRow(rowIndex++);
        boolean isHourly = hourlyData != null;
        headerRow.createCell(0).setCellValue(isHourly ? "小时" : "日期");
        headerRow.createCell(1).setCellValue("订单总数");
        headerRow.createCell(2).setCellValue("有效订单数");
        headerRow.createCell(3).setCellValue("完成率");
        for (int i = 0; i < 4; i++)
            headerRow.getCell(i).setCellStyle(headerStyle);

        if (isHourly) {
            for (HourlyStatisticsDTO dto : hourlyData) {
                XSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(dto.getHour() + ":00");
                row.createCell(1).setCellValue(dto.getTotalCnt());
                row.createCell(2).setCellValue(dto.getValidCnt());
                double rate = dto.getTotalCnt() == 0 ? 0
                        : Math.round((double) dto.getValidCnt() / dto.getTotalCnt() * 10000.0) / 100.0;
                row.createCell(3).setCellValue(rate + "%");
            }
        } else {
            for (DailyStatisticsDTO dto : dailyData) {
                XSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(dto.getDate().toString());
                row.createCell(1).setCellValue(dto.getTotalCnt());
                row.createCell(2).setCellValue(dto.getValidCnt());
                double rate = dto.getTotalCnt() == 0 ? 0
                        : Math.round((double) dto.getValidCnt() / dto.getTotalCnt() * 10000.0) / 100.0;
                row.createCell(3).setCellValue(rate + "%");
            }
        }
        return rowIndex;
    }

    private int writeUserBlock(XSSFSheet sheet, int rowIndex,
            List<DailyStatisticsDTO> dailyData, @NonNull List<HourlyStatisticsDTO> hourlyData,
            XSSFCellStyle titleStyle, XSSFCellStyle headerStyle, XSSFCellStyle dataStyle) {
        rowIndex = addBlockTitle(sheet, rowIndex, "用户统计", titleStyle, 3);

        XSSFRow headerRow = sheet.createRow(rowIndex++);
        boolean isHourly = hourlyData != null;
        headerRow.createCell(0).setCellValue(isHourly ? "小时" : "日期");
        headerRow.createCell(1).setCellValue("新增用户");
        headerRow.createCell(2).setCellValue("累计用户");
        for (int i = 0; i < 3; i++)
            headerRow.getCell(i).setCellStyle(headerStyle);

        if (isHourly) {
            for (HourlyStatisticsDTO dto : hourlyData) {
                XSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(dto.getHour() + ":00");
                row.createCell(1).setCellValue(dto.getDailyNew());
                row.createCell(2).setCellValue(dto.getTotalUsers());
            }
        } else {
            for (DailyStatisticsDTO dto : dailyData) {
                XSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(dto.getDate().toString());
                row.createCell(1).setCellValue(dto.getDailyNew());
                row.createCell(2).setCellValue(dto.getTotalUsers());
            }
        }
        return rowIndex;
    }

    private void writeTop10Block(XSSFSheet sheet, int rowIndex, List<DishSalesDTO> data,
            XSSFCellStyle titleStyle, XSSFCellStyle headerStyle, XSSFCellStyle dataStyle) {
        rowIndex = addBlockTitle(sheet, rowIndex, "销量Top10", titleStyle, 3);

        XSSFRow headerRow = sheet.createRow(rowIndex++);
        headerRow.createCell(0).setCellValue("排名");
        headerRow.createCell(1).setCellValue("菜品名称");
        headerRow.createCell(2).setCellValue("销量");
        for (int i = 0; i < 3; i++)
            headerRow.getCell(i).setCellStyle(headerStyle);

        for (int i = 0; i < data.size(); i++) {
            DishSalesDTO dto = data.get(i);
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(dto.getName());
            row.createCell(2).setCellValue(dto.getSales());
        }
    }

    private XSSFCellStyle createTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private XSSFCellStyle createDataStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private XSSFCellStyle createAmountStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private void autoSizeColumns(XSSFSheet sheet) {
        if (sheet.getPhysicalNumberOfRows() == 0)
            return;
        for (int i = 1; i < 4; i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, Math.min(currentWidth + 1536, 256 * 50));
        }
    }
}
