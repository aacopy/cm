package cn.aacopy.cm.auth.security.service;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Component;

/**
 * @author cmyang
 * @date 2025/10/30
 */
@Component
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        System.out.println("保存用户授权信息：" + authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        System.out.println("删除用户授权信息：" + authorizationConsent);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        System.out.println("根据客户端ID和主体名称查找用户授权信息，registeredClientId：" + registeredClientId + "，principalName：" + principalName);
        return null;
    }
}
