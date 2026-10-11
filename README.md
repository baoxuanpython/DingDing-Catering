---

## 叮叮餐饮 v1.0.0 正式发布 🎉

## 📌 版本简介
这是"叮叮餐饮"系统的首个正式发布版本。项目基于"苍穹外卖"二次开发，从包名、配置、业务逻辑到代码风格均已完成全面重构，形成独立可维护的餐饮管理系统。包含管理端与用户端（微信小程序）两套完整接口，覆盖菜品/套餐管理、订单流转、数据统计等核心业务。

## ✨ 新增功能 (Features)

### 管理端
- **员工管理**：登录/退出、新增（MD5 默认密码）、分页查询（姓名模糊搜索）、启用/禁用、信息编辑、修改密码（校验旧密码 + 新旧密码不能相同）。
- **分类管理**：新增/删除/修改分类，分页查询，按类型（菜品/套餐）筛选，状态切换，删除前校验关联数据。
- **菜品管理**：新增菜品（含口味列表）、分页查询、修改（口味先删后增）、起售/停售、批量删除（拦截已起售菜品、拦截被套餐关联的菜品并返回详情）。
- **套餐管理**：新增套餐（关联菜品列表）、分页查询、修改、起售/停售、批量删除（拦截已起售套餐），启用套餐时自动校验关联菜品是否全部起售。
- **订单管理**：条件搜索（订单号/手机号/状态/时间范围）、订单详情、接单/拒单/派送/完成/取消、订单统计（待接单/待派送/派送中数量）。
- **数据统计**：营业额统计、用户统计（总用户数/新增用户数）、订单统计（订单数/有效订单数/完成率）、销量 Top10 排行、Excel 运营报表导出。
- **工作台概览**：今日运营数据（营业额/有效订单/完成率/客单价/新增用户）、菜品/套餐总览（已启售/已停售数量）、订单概览。
- **文件上传**：阿里云 OSS 图片上传，返回可访问 URL。
- **店铺状态**：获取/设置营业状态（营业中/打烊中），切换时自动清除菜品/套餐缓存。

### 用户端（微信小程序）
- **微信登录**：通过 code 换取 openid，首次登录自动注册，生成 JWT 令牌。
- **菜品/套餐浏览**：按分类查询已起售菜品和已启用套餐，查看套餐内菜品明细。
- **购物车管理**：添加/减少商品、查询列表、清空，数量减至 0 时自动移除。
- **地址簿管理**：新增/修改/删除地址，设置默认地址，查询默认地址。
- **下单支付**：选择收货地址提交订单，清空购物车，支付回调（模拟），WebSocket 推送新订单提醒。
- **订单管理**：历史订单分页查询、订单详情、再来一单（历史订单菜品一键加入购物车）、催单（WebSocket 推送）、用户取消订单。

## 🚀 技术亮点 (Technical Highlights)

| 技术 | 用途 |
|------|------|
| **Spring Boot 3.2.5 + JDK 17** | 项目基础框架，使用最新 LTS 版本 |
| **MyBatis + PageHelper** | 数据持久化 + 物理分页，驼峰映射自动开启 |
| **Redis + JSON 序列化** | 菜品/套餐/店铺状态缓存，使用 `JacksonObjectMapper` 统一序列化配置，解决中文乱码 |
| **JWT + HandlerInterceptor** | 管理端/用户端双通道无状态认证，Token 过期/无效分类处理 |
| **Spring AOP — AutoFill** | 拦截 `@AutoFill` 注解，反射填充 `createTime/updateTime/createUser/updateUser`，缺失 setter 时优雅跳过 |
| **Spring AOP — AutoClearCache** | 拦截 `@AutoClearCache` 注解，方法成功返回后按 `CacheType` 精准清除对应缓存，失败不抛异常 |
| **阿里云 OSS** | 图片文件云端存储，集成 `alibabacloud-oss-v2` SDK |
| **Druid 连接池** | 数据库连接池 + 监控面板（`/druid/*`），配置慢 SQL 阈值 5s |
| **WebSocket** | 管理端实时接收新订单提醒和用户催单提醒 |
| **@Scheduled 定时任务** | 超时未支付订单自动取消（`OrderTask`） |
| **Knife4j (Swagger)** | 接口文档自动生成，支持在线调试 |
| **FastJSON2 + Jackson** | JSON 处理双引擎，Redis 序列化使用统一 Jackson 配置 |
| **@Transactional** | 关键业务方法声明 `rollbackFor = Exception.class`，保证异常回滚 |
| **Maven 多模块** | `common`（工具/常量/异常/注解）/ `pojo`（DTO/VO/Entity）/ `server`（Controller/Service/Mapper）三层分离 |

## 🔧 重构与优化 (Refactor)

- **包名全面替换**：从 `com.sky` 改为 `com.dingdingcatering`，所有模块统一。
- **工程结构精简**：清理冗余 Maven 模块与无用依赖，仅保留 `common`、`pojo`、`server` 三个核心模块。
- **配置外部化**：数据库、Redis、JWT、OSS、微信等敏感配置全部提取为环境变量（`${dingding.*}`），不再硬编码。
- **统一异常处理**：`GlobalExceptionHandler` 覆盖 15+ 种异常类型（业务异常、JWT 异常、SQL 约束冲突、参数校验失败、JSON 序列化异常等），统一返回 `Result` 格式。
- **公共字段自动填充**：抽取 `AutoFillAspect` 切面，消除 Service 层重复的 `setCreateTime/setUpdateUser` 等样板代码。
- **缓存自动清除**：抽取 `AutoClearCacheAspect` 切面，通过 `@AutoClearCache(CacheType.XXX)` 声明式清除缓存，避免手动 `redisTemplate.delete()` 散落在业务代码中。
- **订单详情填充复用**：管理端/用户端共享 `enrichOrderVOListFromAdmin` 工具方法，避免重复的 Stream 聚合逻辑。
- **日志分级**：关键业务操作使用 `info`，分页查询使用 `debug`，异常使用 `warn/error`，便于生产环境按需调整日志级别。
- **依赖注入规范化**：全部使用构造器注入（`final` 字段），移除 `@Autowired` 字段注入。

## ⚠️ 已知问题与下一阶段计划

- [ ] 支付环节当前为模拟回调（`TODO` 标记），后续需接入微信支付真实 API。
- [ ] 前端管理后台（Vue 3 + Vite + Element Plus）正在重构开发中。
- [ ] 微信小程序端（原生开发 + Vant Weapp）待联调。
- [ ] 后续计划引入 Redisson 分布式锁，解决高并发下的订单超卖问题。
- [ ] 部分 Service 方法缺少单元测试覆盖。

## 📦 部署与运行

### 环境要求
- **JDK**：17+
- **MySQL**：8.0+
- **Redis**：7.0+

### 启动步骤
1. 导入 SQL 脚本到 MySQL，创建数据库和表结构。
2. 修改 `application.yml` 中的环境变量（数据库、Redis、JWT 密钥、OSS 配置、微信 AppID/Secret）。
3. 运行 `DingdingCateringApplication.java` 即可启动，默认端口 `8080`。
4. 访问 `http://localhost:8080/doc.html` 查看 Knife4j 接口文档。

### 环境变量清单
| 变量 | 说明 |
|------|------|
| `dingding.datasource.host/port/database/username/password` | 数据库连接 |
| `dingding.redis.host/port` | Redis 连接 |
| `dingding.jwt.admin-secret-key/admin-ttl/admin-token-name` | 管理端 JWT |
| `dingding.jwt.user-secret-key/user-ttl/user-token-name` | 用户端 JWT |
| `dingding.alioss.region/bucket-name/access-key-id/access-key-secret` | 阿里云 OSS |
| `dingding.wechat.appid/secret` | 微信小程序 |
