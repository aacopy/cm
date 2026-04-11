package cn.aacopy.cm.auth.login;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author cmyang
 * @date 2026/2/22
 */
@Component
public class DbRegisteredClientRepository implements RegisteredClientRepository {

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return RegisteredClient.withId("1")
                .clientId("test")
                .clientSecret("{noop}test")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUris(o -> o.addAll(StrUtil.split("http://127.0.0.1:8002", StrUtil.COMMA)))
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                // 是否需要用户同意授权
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                // 刷新token每次提供新的
                .tokenSettings(TokenSettings.builder()
                        .authorizationCodeTimeToLive(Duration.ofSeconds(600))
                        .accessTokenTimeToLive(Duration.ofSeconds(60))
                        .refreshTokenTimeToLive(Duration.ofSeconds(3600))
                        .reuseRefreshTokens(false).build())
                .build();
    }
}
