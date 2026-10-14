-- auto-generated definition
create table address_book
(
    id            bigint auto_increment comment '主键'
        primary key,
    user_id       bigint                       not null comment '用户id',
    consignee     varchar(50)                  null comment '收货人',
    sex           varchar(2)                   null comment '性别',
    phone         varchar(11)                  not null comment '手机号',
    province_code varchar(12) charset utf8mb4  null comment '省级区划编号',
    province_name varchar(32) charset utf8mb4  null comment '省级名称',
    city_code     varchar(12) charset utf8mb4  null comment '市级区划编号',
    city_name     varchar(32) charset utf8mb4  null comment '市级名称',
    district_code varchar(12) charset utf8mb4  null comment '区级区划编号',
    district_name varchar(32) charset utf8mb4  null comment '区级名称',
    detail        varchar(200) charset utf8mb4 null comment '详细地址',
    label         varchar(100) charset utf8mb4 null comment '标签',
    is_default    tinyint(1) default 0         not null comment '默认 0 否 1是'
)
    comment '地址簿' collate = utf8mb3_bin;

-- auto-generated definition
create table employee
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(32)   not null comment '姓名',
    username    varchar(32)   not null comment '用户名',
    password    varchar(64)   not null comment '密码',
    phone       varchar(11)   not null comment '手机号',
    sex         varchar(2)    not null comment '性别',
    id_number   varchar(18)   not null comment '身份证号',
    status      int default 1 not null comment '状态 0:禁用，1:启用',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '更新时间',
    create_user bigint        null comment '创建人',
    update_user bigint        null comment '修改人',
    constraint employee_pk
        unique (id_number),
    constraint employee_username_uindex
        unique (username) comment '唯一列'
)
    comment '员工信息' collate = utf8mb3_bin;

-- auto-generated definition
create table category
(
    id          bigint auto_increment comment '主键'
        primary key,
    type        int           null comment '类型   1 菜品分类 2 套餐分类',
    name        varchar(32)   not null comment '分类名称',
    sort        int default 0 not null comment '顺序',
    status      int           null comment '分类状态 0:禁用，1:启用',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '更新时间',
    create_user bigint        null comment '创建人',
    update_user bigint        null comment '修改人',
    constraint idx_category_name
        unique (name)
)
    comment '菜品及套餐分类' collate = utf8mb3_bin;



-- auto-generated definition
create table orders
(
    id                      bigint auto_increment comment '主键'
        primary key,
    number                  varchar(50)          null comment '订单号',
    status                  int        default 1 not null comment '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
    user_id                 bigint               not null comment '下单用户',
    address_book_id         bigint               not null comment '地址id',
    order_time              datetime             not null comment '下单时间',
    checkout_time           datetime             null comment '结账时间',
    pay_method              int        default 1 not null comment '支付方式 1微信,2支付宝',
    pay_status              tinyint    default 0 not null comment '支付状态 0未支付 1已支付 2退款',
    amount                  decimal(10, 2)       not null comment '实收金额',
    remark                  varchar(100)         null comment '备注',
    phone                   varchar(11)          null comment '手机号',
    address                 varchar(255)         null comment '地址',
    user_name               varchar(32)          null comment '用户名称',
    consignee               varchar(32)          null comment '收货人',
    cancel_reason           varchar(255)         null comment '订单取消原因',
    rejection_reason        varchar(255)         null comment '订单拒绝原因',
    cancel_time             datetime             null comment '订单取消时间',
    estimated_delivery_time datetime             null comment '预计送达时间',
    delivery_status         tinyint(1) default 1 not null comment '配送状态  1立即送出  0选择具体时间',
    delivery_time           datetime             null comment '送达时间',
    pack_amount             int                  null comment '打包费',
    tableware_number        int                  null comment '餐具数量',
    tableware_status        tinyint(1) default 1 not null comment '餐具数量状态  1按餐量提供  0选择具体数量'
)
    comment '订单表' collate = utf8mb3_bin;

-- auto-generated definition
create table setmeal
(
    id          bigint auto_increment comment '主键'
        primary key,
    category_id bigint         not null comment '菜品分类id',
    name        varchar(32)    not null comment '套餐名称',
    price       decimal(10, 2) not null comment '套餐价格',
    status      int default 1  null comment '售卖状态 0:停售 1:起售',
    description varchar(255)   null comment '描述信息',
    image       varchar(255)   null comment '图片',
    create_time datetime       null comment '创建时间',
    update_time datetime       null comment '更新时间',
    create_user bigint         null comment '创建人',
    update_user bigint         null comment '修改人',
    constraint idx_setmeal_name
        unique (name)
)
    comment '套餐' collate = utf8mb3_bin;

create table setmeal_dish
(
    id         bigint auto_increment comment '主键'
        primary key,
    setmeal_id bigint         null comment '套餐id',
    dish_id    bigint         null comment '菜品id',
    name       varchar(32)    null comment '菜品名称 （冗余字段）',
    price      decimal(10, 2) null comment '菜品单价（冗余字段）',
    copies     int            null comment '菜品份数'
)
    comment '套餐菜品关系' collate = utf8mb3_bin;


-- auto-generated definition
create table shop_config
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    config_key   varchar(50)                        not null comment '配置键',
    config_value varchar(255)                       not null comment '配置值',
    description  varchar(500)                       null comment '配置描述',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint config_key
        unique (config_key)
)
    comment '店铺配置表';

create index idx_config_key
    on shop_config (config_key);

-- auto-generated definition
create table user
(
    id          bigint auto_increment comment '主键'
        primary key,
    openid      varchar(45)  null comment '微信用户唯一标识',
    name        varchar(32)  null comment '姓名',
    phone       varchar(11)  null comment '手机号',
    sex         varchar(2)   null comment '性别',
    id_number   varchar(18)  null comment '身份证号',
    avatar      varchar(500) null comment '头像',
    create_time datetime     null
)
    comment '用户信息' collate = utf8mb3_bin;

-- 新的修改
create table flavor
(
    id      bigint auto_increment comment '主键'
        primary key,
    flavor_name    varchar(32)  null comment '口味类型名称',
    value_list   varchar(255) null comment '口味值可选列表'
)
    comment '口味表' collate = utf8mb3_bin;

-- 新的修改（新增了flavors字段，用于存储菜品的口味类型id列表）
create table dish
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(32)    not null comment '菜品名称',
    category_id bigint         not null comment '菜品分类id',
    price       decimal(10, 2) null comment '菜品价格',
    image       varchar(255)   null comment '图片',
    description varchar(255)   null comment '描述信息',
    status      int default 1  null comment '0 停售 1 起售',
    flavors     varchar(255)   null comment '口味类型id列表',
    create_time datetime       null comment '创建时间',
    update_time datetime       null comment '更新时间',
    create_user bigint         null comment '创建人',
    update_user bigint         null comment '修改人',
    constraint idx_dish_name
        unique (name)
)
    comment '菜品' collate = utf8mb3_bin;


-- 新的修改（这是原本的dish_flavor表，但是现在改为了sold表）
create table sold
(
    id      bigint auto_increment comment '主键'
        primary key,
    from_setmeal_id bigint      null comment '来自售出套餐的id',
    from_dish_id bigint        null comment '来自售出菜品的id',
    flavor_id varchar(255)        null comment '口味类型id列表',
    flavor_val varchar(255)       null comment '指定口味值列表'
)
    comment '已售菜品表' collate = utf8mb3_bin;


-- 新的修改（将原本的dish_id、setmeal_id、dish_flavor替换为sold_id_list）
create table order_detail
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(32)    null comment '名字',
    image       varchar(255)   null comment '图片',
    order_id    bigint         not null comment '订单id',
    dish_id     bigint         null comment '菜品id',
    setmeal_id  bigint         null comment '套餐id',
    sold_id_list varchar(255)   null comment '该订单在已售菜品表的id列表',
    number      int default 1  not null comment '数量',
    amount      decimal(10, 2) not null comment '金额'
)
    comment '订单明细表' collate = utf8mb3_bin;

-- auto-generated definition
create table shopping_cart
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(32)    null comment '商品名称',
    image       varchar(255)   null comment '图片',
    user_id     bigint         not null comment '主键',
    dish_id     bigint         null comment '菜品id',
    setmeal_id  bigint         null comment '套餐id',
    flavor_id varchar(255)        null comment '口味类型id列表',
    flavor_val varchar(255)       null comment '指定口味值列表',
    number      int default 1  not null comment '数量',
    amount      decimal(10, 2) not null comment '金额',
    create_time datetime       null comment '创建时间'
)
    comment '购物车' collate = utf8mb3_bin;
