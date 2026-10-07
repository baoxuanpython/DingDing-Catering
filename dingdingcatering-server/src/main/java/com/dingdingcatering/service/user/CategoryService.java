package com.dingdingcatering.service.user;

import com.dingdingcatering.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> list(Integer type);
}
