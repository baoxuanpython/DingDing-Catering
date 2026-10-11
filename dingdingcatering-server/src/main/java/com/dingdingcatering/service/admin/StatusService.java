package com.dingdingcatering.service.admin;

/**
 * 店铺营业状态服务接口
 */
public interface StatusService {

    /**
     * 获取店铺营业状态。
     *
     * @return 店铺状态（1-营业中，0-打烊中）
     */
    Integer getStatus();

    /**
     * 更新店铺营业状态。
     *
     * @param status 目标状态
     */
    void updateStatus(Integer status);
}
