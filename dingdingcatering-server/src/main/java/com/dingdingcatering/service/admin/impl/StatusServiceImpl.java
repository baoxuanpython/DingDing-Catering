package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.mapper.admin.StatusMapper;
import com.dingdingcatering.service.admin.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatusServiceImpl implements StatusService {
    private static final String SHOP_STATUS_KEY = "SHOP_STATUS";
    private final StatusMapper statusMapper;

    public StatusServiceImpl(StatusMapper statusMapper) {
        this.statusMapper = statusMapper;
    }

    @Override
    public Integer getStatus() {
        String value = statusMapper.getValue(SHOP_STATUS_KEY);
        if (value == null) {
            log.warn("店铺状态配置未找到，返回默认值：1（营业）");
            return 1;
        }
        return Integer.parseInt(value);
    }

    @Override
    public void updateStatus(Integer status) {
        int rows = statusMapper.updateValue(SHOP_STATUS_KEY, status.toString());
        if (rows == 0) {
            log.error("更新店铺状态失败，可能配置项不存在");
            throw new RuntimeException("更新店铺状态失败");
        }
        log.info("店铺状态已更新为：{}", status == 1 ? "营业" : "打烊");
    }
}
