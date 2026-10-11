package com.dingdingcatering.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 自定义 Jackson ObjectMapper
 * <p>
 * 扩展 ObjectMapper，配置 Java 8 时间类型（LocalDate、LocalTime、LocalDateTime）
 * 的序列化与反序列化规则，使其与数据库中的日期/时间格式保持一致。
 * <p>
 * 序列化格式：
 * 1. LocalDate → "yyyy-MM-dd"
 * 2. LocalTime → "HH:mm:ss"
 * 3. LocalDateTime → "yyyy-MM-dd HH:mm:ss"
 */
public class JacksonObjectMapper extends ObjectMapper {

    /** 日期格式：yyyy-MM-dd */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /** 日期时间格式：yyyy-MM-dd HH:mm:ss */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 时间格式：HH:mm:ss */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 构造方法，注册 Java 8 时间类型的序列化器和反序列化器
     */
    public JacksonObjectMapper() {
        super();
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        this.registerModule(simpleModule);
    }
}
