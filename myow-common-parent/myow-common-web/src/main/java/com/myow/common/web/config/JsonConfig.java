package com.myow.common.web.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.myow.common.exception.BusinessException;
import com.myow.common.response.ResultCode;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Fastjson2序列化配置
 */
@Configuration
public class JsonConfig {

    private static final Logger logger = LoggerFactory.getLogger(JsonConfig.class);

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.deserializers(new LocalDateDeserializer(DatePattern.NORM_DATE_FORMAT.getDateTimeFormatter()));
            builder.deserializers(new FlexibleLocalDateTimeDeserializer());
            builder.serializers(new LocalDateSerializer(DatePattern.NORM_DATE_FORMAT.getDateTimeFormatter()));
            builder.serializers(new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMAT.getDateTimeFormatter()));
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            builder.serializerByType(BigDecimal.class, ToStringSerializer.instance);
        };
    }

    public static class FlexibleLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

        public FlexibleLocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String text = parser.getText();
            if (StringUtils.isBlank(text)) {
                return null;
            }
            String normalized = text.trim().replace('T', ' ');
            if (normalized.length() == 16) {
                normalized = normalized + ":00";
            }
            return LocalDateTimeUtil.parse(normalized, DatePattern.NORM_DATETIME_FORMAT.getDateTimeFormatter());
        }
    }


    /**
     * string 转为 LocalDateTime 配置类
     */
    @Configuration
    public static class StringToLocalDateTime implements Converter<String, LocalDateTime> {

        @Override
        public LocalDateTime convert(String str) {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            LocalDateTime localDateTime;
            try {
                String normalized = str.trim().replace('T', ' ');
                if (normalized.length() == 16) {
                    normalized = normalized + ":00";
                }
                localDateTime = LocalDateTimeUtil.parse(normalized, DatePattern.NORM_DATETIME_FORMAT.getDateTimeFormatter());
            } catch (DateTimeParseException e) {
                throw new RuntimeException("请输入正确的日期格式：yyyy-MM-dd HH:mm:ss");
            }
            return localDateTime;
        }
    }


    /**
     * string 转为 LocalDate 配置类
     */
    @Configuration
    public static class StringToLocalDate implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String str) {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            LocalDate localDate;
            try {
                localDate = LocalDateTimeUtil.parseDate(str, DatePattern.NORM_DATE_FORMAT.getDateTimeFormatter());
            } catch (DateTimeParseException e) {
                logger.error("请输入正确的日期格式：yyyy-MM-dd");
                throw new BusinessException(ResultCode.SYSTEM_ERROR);
            }
            return localDate;
        }
    }
}
