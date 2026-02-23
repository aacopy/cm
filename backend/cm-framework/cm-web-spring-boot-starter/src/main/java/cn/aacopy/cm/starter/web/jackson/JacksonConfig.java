package cn.aacopy.cm.starter.web.jackson;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

/**
 * @author cmyang
 * @date 2026/1/29
 */
@AutoConfiguration(after = JacksonAutoConfiguration.class)
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                // Long -> String 防止前端精度丢失
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance)
                // 时间
                .modules(new JavaTimeModule())
                // 禁用时间戳
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 指定时区
                .timeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                // 时间格式化
                .simpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
