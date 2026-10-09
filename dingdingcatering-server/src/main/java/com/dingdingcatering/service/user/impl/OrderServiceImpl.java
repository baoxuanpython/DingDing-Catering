package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.mapper.user.OrderMapper;
import com.dingdingcatering.service.user.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("userOrderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public void sendReminder(Long id) {
        log.info("发送订单催单提醒: orderId={}", id);
        orderMapper.sendReminder(id);
        log.info("发送订单催单提醒成功: orderId={}", id);
    }
}
