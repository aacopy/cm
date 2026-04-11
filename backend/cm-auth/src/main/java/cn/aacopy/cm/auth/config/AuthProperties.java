package cn.aacopy.cm.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cmyang
 * @date 2026/2/15
 */
@Data
@ConfigurationProperties(prefix = "cm.auth")
public class AuthProperties {

    private String domain; // 域名
    private Pwd pwd; // 密码
    private Jwk jwk; // JWK

    @Data
    public static class Pwd {
        private String privateKey;
        private String publicKey;
    }

    @Data
    public static class Jwk {
        private String kid;
        private String privateKey;
        private String publicKey;
    }
}
