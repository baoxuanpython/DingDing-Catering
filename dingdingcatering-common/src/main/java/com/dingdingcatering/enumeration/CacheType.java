package com.dingdingcatering.enumeration;

public enum CacheType {
    CATEGORY("category"),
    DISH("dish"),
    SETMEAL_CATEGORY("setmeal:category"),
    SETMEAL_DISH("setmeal:dish"),
    SHOP_STATUS("shopStatus");

    private final String cacheName;

    CacheType(String cacheName) {
        this.cacheName = cacheName;
    }

    public String cacheName() {
        return cacheName;
    }
}
