package com.sky.service.admin.impl;

import com.sky.mapper.admin.ReportMapper;
import com.sky.service.admin.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;
    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }
}
