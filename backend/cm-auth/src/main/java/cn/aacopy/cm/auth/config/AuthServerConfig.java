package cn.aacopy.cm.auth.config;

import cn.aacopy.cm.auth.login.emailcode.EmailCodeAuthenticationFilter;
import cn.aacopy.cm.auth.login.sso.OidcUserInfoMapper;
import cn.aacopy.cm.auth.login.sso.SsoFilter;
import cn.aacopy.cm.auth.login.sso.SsoService;
import cn.hutool.crypto.asymmetric.RSA;
import com.nimbusds.jose.jwk.JWK;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.annotation.Resource;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器的配置类
 * @author cmyang
 * @date 2026/2/15
 */
@Configuration
public class AuthServerConfig {

    @Resource
    private AuthProperties authProperties;

    /**
     * OIDC和单点登录
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OidcUserInfoMapper oidcUserInfoMapper,
                                                                      SsoService ssoService) throws Exception {
        // 引入默认的授权服务器配置（包含 OIDC 端点：/.well-known/openid-configuration, /oauth2/jwks, /userinfo, /oidc/logout 等）
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 开启 OIDC
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 开启oidc
                .oidc(oidc ->
                        oidc.userInfoEndpoint(userInfo ->
                                userInfo.userInfoMapper(oidcUserInfoMapper)));
        // 关闭默认的session机制
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 资源服务器配置，保护授权服务器的端点，要求携带有效的访问令牌
        http.oauth2ResourceServer(resourceServer ->
                resourceServer.jwt(Customizer.withDefaults()));
        // 通过Cookie验证单点登录
        http.addFilterAfter(new SsoFilter(ssoService), SecurityContextPersistenceFilter.class);
        return http.build();
    }

    /**
     * 登录
     */
    @Bean
    @Order(2)
    public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http,
                                                        EmailCodeAuthenticationFilter emailCodeAuthenticationFilter) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .requestMatchers((requestMatchers) ->  requestMatchers.antMatchers(
                        "/login/emailCode"
                ))
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated());
        // 关闭默认的session机制
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 把容器里的自定义的登录拦截器加到UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(emailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 将自定义的providers注册到AuthenticationManager中
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, List<AuthenticationProvider> providers) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        for (AuthenticationProvider p : providers) {
            builder.authenticationProvider(p);
        }
        return builder.build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSA rsa = new RSA(authProperties.getJwk().getPrivateKey(), authProperties.getJwk().getPublicKey());
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) rsa.getPublicKey())
                .privateKey(rsa.getPrivateKey())
                .keyID(authProperties.getJwk().getKid())
                .build();
        List<JWK> jwks = new ArrayList<>();
        jwks.add(rsaKey);
        JWKSet jwkSet = new JWKSet(jwks);
        return new ImmutableJWKSet<>(jwkSet);
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
     * token生成器
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
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
