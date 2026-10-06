-- 店铺配置表
CREATE TABLE IF NOT EXISTS shop_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    config_key VARCHAR(50) NOT NULL UNIQUE COMMENT '配置键',
    config_value VARCHAR(255) NOT NULL COMMENT '配置值',
    description VARCHAR(500) COMMENT '配置描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺配置表';

-- 初始化数据：店铺状态（1-营业 0-打烊）
INSERT INTO shop_config (config_key, config_value, description) VALUES
('SHOP_STATUS', '1', '店铺营业状态：0-打烊 1-营业')
ON DUPLICATE KEY UPDATE update_time = NOW();
