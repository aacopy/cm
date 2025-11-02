package cn.aacopy.cm.auth.security.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author cmyang
 * @date 2025/10/30
 */
@Component
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    @Resource(name = "javaValueRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    // Token ID Redis键前缀
    private static final String REDIS_CONSENT_PREFIX = "AUTH:CONSENT:";
    // 授权信息过期时间，单位：天
    private static final Integer REDIS_CONSENT_EXPIRE_DAYS = 30;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String redisKey = generateRedisKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        redisTemplate.opsForValue().set(redisKey, authorizationConsent, REDIS_CONSENT_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String redisKey = generateRedisKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        redisTemplate.delete(redisKey);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        String redisKey = generateRedisKey(registeredClientId, principalName);
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 生成Redis键
     * @param registeredClientId
     * @param principalName
     * @return
     */
    private String generateRedisKey(String registeredClientId, String principalName) {
        return REDIS_CONSENT_PREFIX + registeredClientId + ":" + principalName;
    }
}
