package cn.aacopy.cm.auth;

import cn.aacopy.cm.auth.config.AuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author cmyang
 * @date 2026/2/15
 */
@SpringBootApplication
@EnableConfigurationProperties(AuthProperties.class)
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
