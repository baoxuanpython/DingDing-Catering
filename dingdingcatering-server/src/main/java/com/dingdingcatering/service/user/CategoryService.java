package com.dingdingcatering.service.user;

import com.dingdingcatering.entity.Category;

import java.util.List;

/**
 * C端分类服务接口
 */
public interface CategoryService {
    /**
     * 查询启用状态的分类列表。
     *
     * @param type 分类类型筛选
     * @return 启用状态的分类列表
     */
    List<Category> list(Integer type);
}
