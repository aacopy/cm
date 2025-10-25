package cn.aacopy.cm.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类
 * @author cmyang
 * @date 2025/10/24
 */
@Configuration
@MapperScan("cn.aacopy.cm.auth.mapper")
public class MyBatisPlusConfig {
}
