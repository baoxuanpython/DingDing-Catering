package com.sky.service.admin.impl;

import com.sky.mapper.admin.OrderMapper;
import com.sky.service.admin.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }
}
