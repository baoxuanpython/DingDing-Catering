package com.dingdingcatering.service.user.Impl;

import com.dingdingcatering.mapper.user.ShopMapper;
import com.dingdingcatering.service.user.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    private final ShopMapper shopMapper;
    public ShopServiceImpl(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }
}
