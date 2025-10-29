package cn.aacopy.cm.auth.security.service;

import cn.aacopy.cm.auth.model.entity.ClientDO;
import cn.aacopy.cm.auth.model.enums.CommonStatusEnum;
import cn.aacopy.cm.auth.service.ClientService;
import cn.hutool.core.util.StrUtil;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 数据库 RegisteredClientRepository 实现类
 * @author cmyang
 * @date 2025/10/25
 */
@Component
public class DbRegisteredClientRepository implements RegisteredClientRepository {

    @Resource
    private ClientService clientService;

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("Not allowed to save this entity");
    }

    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException("Not allowed to find this entity by id");
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        // 从数据库查询客户端信息
        ClientDO clientDO = clientService.findByClientId(clientId);
        // 转换为RegisteredClient对象并返回
        return RegisteredClient.withId(clientDO.getId().toString())
                .clientId(clientDO.getClientId()) // 客户端ID
                .clientSecret(clientDO.getClientSecret()) // 客户端密钥
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // 客户端认证方法
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // 授权码模式
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // 刷新令牌模式
                .redirectUris(o -> o.addAll(StrUtil.split(clientDO.getRedirectUris(), StrUtil.COMMA))) // 重定向URI
                .scope(OidcScopes.OPENID) // OpenID Connect作用域
                .scope(OidcScopes.PROFILE) // profile作用域
                // 是否需要用户同意授权
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(CommonStatusEnum.isEnabled(clientDO.getConsent())) // 是否需要用户同意授权
                        .build()) // 客户端设置
                // 刷新token每次提供新的
                .tokenSettings(TokenSettings.builder()
                        .reuseRefreshTokens(false) // 刷新令牌每次提供新的
                        .build()) // 令牌设置
                .build();
    }
}
