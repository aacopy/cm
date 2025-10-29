package cn.aacopy.cm.auth.config;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis配置
 * @author cmyang
 * @date 2025/10/28
 */
@Configuration
public class RedisConfig {

    /**
     * Value为JSON序列化的RedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 解决时间序列化报错
        RedisSerializer<Object> json = RedisSerializer.json();
        ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(json, "mapper");
        objectMapper.registerModules(new JavaTimeModule());

        // Key使用String序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());

        // Value使用JSON序列化
        template.setValueSerializer(json);
        template.setHashValueSerializer(json);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Value为Java序列化的RedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean("javaValueRedisTemplate")
    public RedisTemplate<String, Object> javaValueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Key使用String序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());

        // Value使用Java序列化
        template.setValueSerializer(RedisSerializer.java());
        template.setHashValueSerializer(RedisSerializer.java());

        template.afterPropertiesSet();
        return template;
    }
}
