package com.dingdingcatering.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.dingdingcatering.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Web MVC 配置�?
 * <p>
 * 用于注册和配�?Web 层的核心组件，包括：
 * <ul>
 *     <li>JWT 令牌校验拦截器：保护管理�?API 接口安全</li>
 *     <li>自定义消息转换器：统一处理 Java 8 日期时间类型的序列化和反序列�?/li>
 * </ul>
 *
 * <h2>核心功能�?/h2>
 * <ol>
 *     <li><strong>拦截器管�?/strong>：配置请求拦截规则，实现基于 JWT 的身份认�?/li>
 *     <li><strong>消息转换器定�?/strong>：解�?Java 8 LocalDateTime �?JSON 之间的格式转换问�?/li>
 * </ol>
 *
 * <h2>使用示例�?/h2>
 * <pre>{@code
 * // 此类通过 @Configuration 注解自动�?Spring 加载
 * // 无需手动调用，Spring Boot 启动时自动生�?
 *
 * // 配置生效后，所�?/admin/** 路径的请求都需要携带有�?JWT 令牌
 * // 日期时间字段将统一格式化为 "yyyy-MM-dd HH:mm:ss" 格式
 * }</pre>
 *
 * <h2>设计原则�?/h2>
 * <ul>
 *     <li>单一职责：专注于 Web 层配置，不涉及业务逻辑</li>
 *     <li>开闭原则：通过扩展点（WebMvcConfigurer）增强框架功能，而非修改源码</li>
 *     <li>配置集中化：所�?Web 层相关配置集中在此类中管�?/li>
 * </ul>
 *
 * @author baoxuanpython
 * @version 1.0
 * @since 2024-07-01
 * @see WebMvcConfigurer Spring MVC 配置接口
 * @see JwtTokenAdminInterceptor JWT 令牌校验拦截�?
 * @see MappingJackson2HttpMessageConverter JSON 消息转换�?
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * JWT 令牌校验拦截器实�?
     * <p>
     * 通过构造器注入，由 Spring IoC 容器负责创建和管理生命周期�?
     * 该拦截器用于验证每个请求是否携带有效�?JWT 令牌�?
     */
    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 构造函数：通过依赖注入获取 JWT 拦截器实�?
     *
     * @param jwtTokenAdminInterceptor JWT 令牌校验拦截器（�?Spring 自动注入�?
     */
    public WebMvcConfiguration(JwtTokenAdminInterceptor jwtTokenAdminInterceptor) {
        this.jwtTokenAdminInterceptor = jwtTokenAdminInterceptor;
        log.info("WebMvcConfiguration 初始化完成，JWT 拦截器已注入");
    }

    /**
     * 注册自定义拦截器�?Spring MVC 拦截器链
     * <p>
     * 配置 JWT 令牌校验拦截器的拦截规则�?
     * <ul>
     *     <li><strong>拦截路径</strong>：所有以 {@code /admin/} 开头的请求路径</li>
     *     <li><strong>排除路径</strong>：管理员登录接口（{@code /admin/employee/login}）无需令牌</li>
     * </ul>
     *
     * <h3>拦截流程�?/h3>
     * <pre>
     * 客户端请�?�?到达 DispatcherServlet �?经过拦截器链
     *      �?
     *      ├── 路径匹配 /admin/** ?
     *      �?      �?
     *      �?      ├── �?�?是否在排除列表中�?
     *      �?      �?      �?
     *      �?      �?      ├── 是（如登录）�?放行 �?执行 Controller 方法 �?
     *      �?      �?      �?
     *      �?      �?      └── �?�?执行 JwtTokenAdminInterceptor.preHandle()
     *      �?      �?              �?
     *      �?      �?              ├── Token 有效且未过期 �?放行 �?
     *      �?      �?              �?
     *      �?      �?              └── Token 无效或缺�?�?返回 401 未授�?�?
     *      �?      �?
     *      �?      └── 否（�?/admin/** 路径）→ 直接放行 �?
     *      �?
     *      └── 继续后续处理...
     * </pre>
     *
     * <h3>实际应用场景�?/h3>
     * <ul>
     *     <li>管理员登录：POST /admin/employee/login �?排除拦截，直接访�?/li>
     *     <li>查询订单：GET /admin/order/page �?需要携带有�?JWT 令牌</li>
     *     <li>更新套餐：PUT /admin/setmeal �?需要携带有�?JWT 令牌</li>
     * </ul>
     *
     * @param registry 拦截器注册表，用于添加和管理拦截�?
     *                �?Spring MVC 框架自动传入
     * @see InterceptorRegistry Spring MVC 拦截器注册表
     * @see JwtTokenAdminInterceptor#preHandle 拦截器的具体实现逻辑
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截�?..");

        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")                          // 拦截所有管理端接口
                .excludePathPatterns("/admin/employee/login");         // 排除登录接口

        log.info("自定义拦截器注册完成 - 拦截路径: /admin/**, 排除路径: /admin/employee/login");
    }

    /**
     * 扩展 Spring MVC �?HTTP 消息转换�?
     * <p>
     * 自定�?Jackson JSON 消息转换器，用于统一处理 Java 8 日期时间类型（LocalDateTime�?
     * �?JSON 字符串之间的序列化（Java 对象 �?JSON）和反序列化（JSON �?Java 对象）�?
     *
     * <h2>为什么需要此配置�?/h2>
     * <p>
     * 默认情况下，Jackson 库将 {@code LocalDateTime} 序列化为如下格式�?
     * <pre>
     * {
     *   "orderTime": {
     *     "date": {"year": 2024, "month": 7, "day": 1},
     *     "time": {"hour": 15, "minute": 30, "second": 0}
     *   }
     * }
     * </pre>
     * <p>
     * 这种格式存在以下问题�?
     * <ul>
     *     <li>数据冗余：一个时间字段占用大�?JSON 空间</li>
     *     <li>前端处理复杂：JavaScript 需要额外解析嵌套对�?/li>
     *     <li>可读性差：不利于调试和日志查�?/li>
     * </ul>
     *
     * <h2>配置后的效果�?/h2>
     * <p>
     * 通过本方法配置后，日期时间将被格式化为简洁易读的形式�?
     * <pre>
     * {
     *   "orderTime": "2024-07-01 15:30:00"
     * }
     * </pre>
     *
     * <h2>技术实现细节：</h2>
     * <ol>
     *     <li>创建 {@link MappingJackson2HttpMessageConverter} 实例作为 JSON 转换�?/li>
     *     <li>配置 {@link ObjectMapper} 对象（Jackson 核心类）</li>
     *     <li>注册 {@link JavaTimeModule} 模块以支�?Java 8 时间类型</li>
     *     <li>定义日期时间格式�?{@code yyyy-MM-dd HH:mm:ss}</li>
     *     <li>添加序列化器（LocalDateTime �?String）和反序列化器（String �?LocalDateTime�?/li>
     *     <li>将自定义转换器插入到转换器列表的首位（索�?0），确保优先使用</li>
     * </ol>
     *
     * <h2>支持的日期时间格式：</h2>
     * <table border="1">
     *     <tr><th>格式</th><th>示例</th><th>说明</th></tr>
     *     <tr><td>yyyy-MM-dd HH:mm:ss</td><td>2024-07-01 15:30:00</td><td>标准格式（当前使用）</td></tr>
     *     <tr><td>yyyy-MM-dd'T'HH:mm:ss</td><td>2024-07-01T15:30:00</td><td>ISO 8601 格式</td></tr>
     *     <tr><td>yyyy/MM/dd HH:mm:ss</td><td>2024/07/01 15:30:00</td><td>斜杠分隔格式</td></tr>
     * </table>
     *
     * <h2>影响范围�?/h2>
     * <p>
     * 此配置会影响以下场景�?
     * <ul>
     *     <li><strong>响应体序列化</strong>：Controller 返回对象中的 LocalDateTime 字段</li>
     *     <li><strong>请求体反序列�?/strong>：前端提�?JSON 中的日期字符串转换为 LocalDateTime</li>
     *     <li><strong>全局生效</strong>：对所�?Controller 接口统一生效</li>
     * </ul>
     *
     * <h3>示例：Controller 返回数据</h3>
     * <pre>{@code
     * // 后端实体�?
     * public class OrderVO {
     *     private LocalDateTime orderTime;  // 会被自动格式�?
     * }
     *
     * // 前端接收到的 JSON
     * {
     *     "orderTime": "2024-07-01 15:30:00"  // 格式化的字符�?
     * }
     * }</pre>
     *
     * <h3>示例：前端提交数�?/h3>
     * <pre>{@code
     * // 前端提交�?JSON
     * {
     *     "beginTime": "2024-07-01 00:00:00",
     *     "endTime": "2024-07-31 23:59:59"
     * }
     *
     * // 后端 DTO 自动转换�?LocalDateTime 类型
     * public class OrdersPageQueryDTO {
     *     private LocalDateTime beginTime;  // 自动从字符串转换
     *     private LocalDateTime endTime;
     * }
     * }</pre>
     *
     * @param converters Spring MVC 的消息转换器列表（由框架维护�?
     *                   包含框架默认注册的各种转换器（JSON、XML、String 等）
     *                   我们的自定义转换器将被插入到此列表的索引 0 位置
     * @see MappingJackson2HttpMessageConverter Jackson JSON 消息转换�?
     * @see ObjectMapper Jackson 核心对象映射�?
     * @see JavaTimeModule Java 8 日期时间支持模块
     * @see DateTimeFormatter 日期时间格式化工�?
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换�?..统一日期格式化为 yyyy-MM-dd HH:mm:ss...");

        /*
         * ============================================================
         * 步骤 1：创�?JSON 消息转换�?
         * ============================================================
         *
         * MappingJackson2HttpMessageConverter �?Spring MVC 处理 JSON 数据的核心组件，
         * 负责�?
         * - �?Controller 返回�?Java 对象序列化为 JSON 字符串（响应体）
         * - 将请求体中的 JSON 字符串反序列化为 Java 对象
         */
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        /*
         * ============================================================
         * 步骤 2：配�?ObjectMapper（Jackson 核心配置对象�?
         * ============================================================
         *
         * ObjectMapper �?Jackson 库的核心类，控制着所有的序列�?反序列化行为�?
         * 通过配置 ObjectMapper，我们可以：
         * - 注册自定义模块（�?JavaTimeModule�?
         * - 设置日期格式、时区等属�?
         * - 配置序列化特性（如忽�?null 值、格式化输出等）
         */
        ObjectMapper objectMapper = new ObjectMapper();

        /*
         * ============================================================
         * 步骤 3：创�?JavaTimeModule 模块
         * ============================================================
         *
         * JavaTimeModule 是专门用于处�?Java 8 日期时间 API（java.time 包）的模块�?
         * 默认情况下，Jackson 不支�?LocalDateTime、LocalDate、LocalTime 等类型，
         * 必须显式注册此模块才能正确处理这些类型�?
         *
         * 支持的类型包括：
         * - LocalDateTime：日期和时间（如订单时间�?
         * - LocalDate：仅日期（如生日�?
         * - LocalTime：仅时间（如营业时间�?
         * - Instant、Duration、Period �?
         */
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        /*
         * ============================================================
         * 步骤 4：定义日期时间格�?
         * ============================================================
         *
         * DateTimeFormatter.ofPattern() 创建指定格式的格式化器�?
         * 格式说明�?
         * - yyyy�? 位年份（2024�?
         * - MM�? 位月份（07 表示 7 月）
         * - dd�? 位日期（01 表示 1 号）
         * - HH�?4 小时制的小时�?5 表示下午 3 点）
         * - mm：分钟（30�?
         * - ss：秒�?0�?
         *
         * 注意事项�?
         * - MM �?mm 不同：MM 是月份，mm 是分�?
         * - HH �?24 小时制，hh �?12 小时�?
         */
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        /*
         * ============================================================
         * 步骤 5：添加序列化器（Java 对象 �?JSON 字符串）
         * ============================================================
         *
         * LocalDateTimeSerializer 负责�?Java �?LocalDateTime 对象
         * 转换为指定格式的 JSON 字符串�?
         *
         * 工作原理�?
         * �?Controller 返回包含 LocalDateTime 字段的对象时�?
         * Jackson 会自动调用此序列化器进行转换�?
         *
         * 示例�?
         * Java 对象: LocalDateTime.of(2024, 7, 1, 15, 30, 0)
         * JSON 输出: "2024-07-01 15:30:00"
         */
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));

        /*
         * ============================================================
         * 步骤 6：添加反序列化器（JSON 字符�?�?Java 对象�?
         * ============================================================
         *
         * LocalDateTimeDeserializer 负责�?JSON 字符串中的日期时�?
         * 解析�?Java �?LocalDateTime 对象�?
         *
         * 工作原理�?
         * 当前端提交包含日期字符串�?JSON 时，
         * Jackson 会自动调用此反序列化器进行转换�?
         *
         * 示例�?
         * JSON 输入: "2024-07-01 15:30:00"
         * Java 对象: LocalDateTime.of(2024, 7, 1, 15, 30, 0)
         *
         * 错误处理�?
         * 如果日期格式不符合要求，会抛�?JsonProcessingException�?
         * Spring MVC 会将其转换为 400 Bad Request 响应�?
         */
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        /*
         * ============================================================
         * 步骤 7：注册模块到 ObjectMapper
         * ============================================================
         *
         * 将配置好�?JavaTimeModule 注册�?ObjectMapper 中�?
         * 这样 ObjectMapper 就知道如何处�?LocalDateTime 类型了�?
         *
         * 除了 JavaTimeModule，还可以注册其他模块�?
         * - Jdk8Module：支�?Optional、Stream 等类�?
         * - ParameterNamesModule：支持多参数构造函�?
         * - JodaModule：支�?Joda-Time 库（旧版日期时间库）
         */
        objectMapper.registerModule(javaTimeModule);

        /*
         * ============================================================
         * 步骤 8：将配置好的 ObjectMapper 设置到转换器�?
         * ============================================================
         *
         * 使消息转换器使用我们自定义的 ObjectMapper 进行 JSON 处理�?
         * 这样每次进行 JSON 序列�?反序列化时都会应用我们的配置�?
         */
        converter.setObjectMapper(objectMapper);

        /*
         * ============================================================
         * 步骤 9：将自定义转换器添加到转换器列表
         * ============================================================
         *
         * converters.add(0, converter) 的含义：
         * - 参数 0：表示插入位置为列表的第一个元�?
         * - 效果：我们的自定义转换器会优先于框架默认的转换器被使�?
         *
         * 为什么放在第一位？
         * Spring MVC 在处理请求时，会按照 converters 列表的顺序依次尝�?
         * 每个转换器，找到第一个能处理的转换器就使用它�?
         * 将我们自定义的转换器放在最前面，可以确保：
         * - 我们的日期格式化配置一定会生效
         * - 不会被其他默认转换器抢先处理
         *
         * 注意：如�?converters.add(converter) 不指定索引，
         * 则会追加到列表末尾，可能不会生效�?
         */
        converters.add(0, converter);

        log.info("消息转换器扩展完�?- 已注册自定义 Jackson 转换器，日期格式: yyyy-MM-dd HH:mm:ss");
    }
}
