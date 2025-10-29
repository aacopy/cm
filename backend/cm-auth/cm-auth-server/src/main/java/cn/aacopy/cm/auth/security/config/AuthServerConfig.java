package cn.aacopy.cm.auth.security.config;

import cn.aacopy.cm.auth.security.filter.CookieAuthenticationFilter;
import cn.aacopy.cm.auth.security.point.LoginAuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 认证服务器配置类
 *
 * @author cmyang
 * @date 2025/10/24
 */
@Configuration
public class AuthServerConfig {

    /**
     * 配置授权服务器端点的安全策略，启用默认的 OAuth2/OIDC 设置以及基于 JWT 的用户信息接口。
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      CookieAuthenticationFilter cookieAuthenticationFilter) throws Exception {
        // 引入默认的授权服务器配置（包含 OIDC 端点：/.well-known/openid-configuration, /oauth2/jwks, /userinfo, /oidc/logout 等）
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 开启 OIDC
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 开启oidc
                .oidc(Customizer.withDefaults());
        // 设置用户同意授权页面
//                .authorizationEndpoint(authorizationEndpoint ->
//                        authorizationEndpoint.consentPage("/oauth2/consent"));
        // 关闭会话，使用无状态的 token 认证
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 自定义登录页
        http.exceptionHandling((exceptions) ->
                exceptions.authenticationEntryPoint(new LoginAuthenticationEntryPoint()));

        http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()));
        // 通过Cookie验证单点登录
        http.addFilterAfter(cookieAuthenticationFilter, SecurityContextPersistenceFilter.class);
        return http.build();
    }

    /**
     * 配置应用自身的表单登录与访问控制（如用户同意页面），并开放健康检查端点。
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          List<AbstractAuthenticationProcessingFilter> processingFilters) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/aabb").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
        // 把容器里的自定义的登录拦截器加到UsernamePasswordAuthenticationFilter之前
        for (AbstractAuthenticationProcessingFilter processingFilter : processingFilters) {
            http.addFilterBefore(processingFilter, UsernamePasswordAuthenticationFilter.class);
        }
        return http.build();
    }

    /**
     * 将自定义的providers注册到AuthenticationManager中
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, List<AuthenticationProvider> providers) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.parentAuthenticationManager(null); // 去掉默认的DaoAuthenticationProvider，异常后ProviderManager会尝试父级执行
        for (AuthenticationProvider p : providers) {
            builder.authenticationProvider(p);
        }
        return builder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://127.0.0.1:5173"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true); // allow cookies / Authorization header
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 不同类型的token生成器
     *
     * @param jwtEncoder
     * @return
     */
    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JwtEncoder jwtEncoder) {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    /**
     * issuer、端点等
     *
     * @return
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
