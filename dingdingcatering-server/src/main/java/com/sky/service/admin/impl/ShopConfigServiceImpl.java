package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.mapper.admin.ShopConfigMapper;
import com.dingdingcatering.service.admin.ShopConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShopConfigServiceImpl implements ShopConfigService {
    private static final String SHOP_STATUS_KEY = "SHOP_STATUS";
    private final ShopConfigMapper shopConfigMapper;

    public ShopConfigServiceImpl(ShopConfigMapper shopConfigMapper) {
        this.shopConfigMapper = shopConfigMapper;
    }

    @Override
    public Integer getShopStatus() {
        String value = shopConfigMapper.getValue(SHOP_STATUS_KEY);
        if (value == null) {
            log.warn("店铺状态配置未找到，返回默认值：1（营业）");
            return 1;
        }
        return Integer.parseInt(value);
    }

    @Override
    public void updateShopStatus(Integer status) {
        int rows = shopConfigMapper.updateValue(SHOP_STATUS_KEY, status.toString());
        if (rows == 0) {
            log.error("更新店铺状态失败，可能配置项不存在");
            throw new RuntimeException("更新店铺状态失�?);
        }
        log.info("店铺状态已更新为：{}", status == 1 ? "营业" : "打烊");
    }
}
