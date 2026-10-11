package com.dingdingcatering.enumeration;

/**
 * 缓存类型枚举
 * <p>
 * 定义系统中所有缓存的名称，配合 @AutoClearCache 注解和 AutoClearCacheAspect 切面使用，
 * 在数据变更后自动清除对应缓存，保证数据一致性。
 */
public enum CacheType {
    /** 菜品分类缓存 */
    CATEGORY("category"),
    /** 菜品缓存 */
    DISH("dish"),
    /** 套餐分类缓存 */
    SETMEAL_CATEGORY("setmeal:category"),
    /** 套餐菜品关联缓存 */
    SETMEAL_DISH("setmeal:dish"),
    /** 店铺营业状态缓存 */
    SHOP_STATUS("shopStatus");

    private final String cacheName;

    CacheType(String cacheName) {
        this.cacheName = cacheName;
    }

    /**
     * 获取缓存名称，用于 Spring Cache 操作
     *
     * @return 缓存名称
     */
    public String cacheName() {
        return cacheName;
    }
}
