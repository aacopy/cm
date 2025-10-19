package cn.aacopy.cm.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 授权服务器核心配置
 *
 * @author cmyang
 * @date 2025/10/19
 */
@Configuration
public class AuthServerConfig {

    /**
     * 配置授权服务器端点的安全策略，启用默认的 OAuth2/OIDC 设置以及基于 JWT 的用户信息接口。
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 引入 Spring Authorization Server 推荐的默认安全配置（端点、安全过滤器等）
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
        // 当访问受保护端点且未登录时重定向到 /login 进行身份验证
        http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
        // 允许授权服务器内部使用 JWT 解析器处理用户信息端点等资源请求
        http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * 配置应用自身的表单登录与访问控制（如用户同意页面），并开放健康检查端点。
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        // 对外开放 Actuator 健康检查等端点
                        .mvcMatchers("/actuator/**").permitAll()
                        // 其余请求均需认证
                        .anyRequest().authenticated())
                // 使用自定义的 UserDetailsService 获取用户信息
                .userDetailsService(userDetailsService)
                // 保留默认表单登录流程
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /**
     * 使用 JDBC 存储注册客户端信息。
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 将授权信息（授权码、token 等）持久化到数据库，保证重启后仍可查询。
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        // 使用 JDBC 持久化授权信息，确保多节点或重启场景下的可用性
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 记录用户对客户端授权范围的同意结果，避免重复弹出授权确认。
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        // 使用 JDBC 存储用户对客户端授予的权限范围
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 暴露授权服务器元信息配置（issuer、端点地址等），默认值适用于本地环境。
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 提供 JWKS 端点所需的 RSA 密钥对，用于签名颁发的访问令牌和 ID 令牌。
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        // 提供 JWK 集合供授权服务器公开的 /.well-known/jwks.json 使用
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /**
     * 基于 JWKS 配置 JWT 解码器，用于校验用户信息端点返回的 JWT。
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        // 使用授权服务器提供的工具基于 JWK 源创建解码器
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * BCrypt 密码编码器，供客户端密钥和用户密码编码使用。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 生成 2048 位 RSA 密钥对，用于签名令牌
     */
    private static RSAKey generateRsa() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Failed to generate RSA key pair", ex);
        }
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                // 使用随机生成的 Key ID，便于 JWKS 端点区分不同密钥
                .keyID(java.util.UUID.randomUUID().toString())
                .build();
    }
}
