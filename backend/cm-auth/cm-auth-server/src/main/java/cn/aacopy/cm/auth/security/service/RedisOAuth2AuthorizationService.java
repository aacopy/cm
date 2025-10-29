package cn.aacopy.cm.auth.security.service;

import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Redis存储OAuth2Authorization实现
 * @author cmyang
 * @date 2025/10/28
 */
@Component
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    // 使用Java序列化的RedisTemplate
    @Resource(name = "javaValueRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    // Token ID Redis键前缀
    private static final String REDIS_OAUTH2_ID_PREFIX = "AUTH:OAUTH2:ID:";
    // Access Token Redis键前缀
    private static final String REDIS_OAUTH2_AT_PREFIX = "AUTH:OAUTH2:AT:";
    // Refresh Token Redis键前缀
    private static final String REDIS_OAUTH2_RT_PREFIX = "AUTH:OAUTH2:RT:";
    // Authorization Code Redis键前缀
    private static final String REDIS_OAUTH2_CD_PREFIX = "AUTH:OAUTH2:CD:";

    @Override
    public void save(OAuth2Authorization authorization) {
        if (authorization == null) {
            throw new OAuth2AuthorizationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN));
        }
        String id = authorization.getId();

        // 处理授权码模式
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            long cdExpiresIn = ChronoUnit.SECONDS.between(authorizationCode.getToken().getIssuedAt(), authorizationCode.getToken().getExpiresAt());
            // Key ID , Value OAuth2Authorization Java对象
            redisTemplate.opsForValue().set(REDIS_OAUTH2_ID_PREFIX + id, authorization, cdExpiresIn, TimeUnit.SECONDS);
            // Key Authorization Code , Value OAuth2Authorization ID
            redisTemplate.opsForValue().set(REDIS_OAUTH2_ID_PREFIX + authorizationCode.getToken().getTokenValue(), id, cdExpiresIn, TimeUnit.SECONDS);
            return;
        }

        // 处理Access Token和Refresh Token
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (accessToken == null || refreshToken == null) {
            throw new OAuth2AuthorizationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN));
        }
        // Access Token过期时间
        long atExpiresIn = ChronoUnit.SECONDS.between(accessToken.getToken().getIssuedAt(), accessToken.getToken().getExpiresAt());
        // Refresh Token过期时间
        long rtExpiresIn = ChronoUnit.SECONDS.between(refreshToken.getToken().getIssuedAt(), refreshToken.getToken().getExpiresAt());
        // Key ID , Value OAuth2Authorization Java对象
        redisTemplate.opsForValue().set(REDIS_OAUTH2_ID_PREFIX + id, authorization, rtExpiresIn, TimeUnit.SECONDS);
        // Key Access Token , Value OAuth2Authorization ID
        redisTemplate.opsForValue().set(REDIS_OAUTH2_AT_PREFIX + accessToken.getToken().getTokenValue(), id, atExpiresIn, TimeUnit.SECONDS);
        // Key Refresh Token , Value OAuth2Authorization ID
        redisTemplate.opsForValue().set(REDIS_OAUTH2_RT_PREFIX + refreshToken.getToken().getTokenValue(), id, rtExpiresIn, TimeUnit.SECONDS);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        // 删除所有相关缓存
        String id = authorization.getId();
        redisTemplate.delete(REDIS_OAUTH2_ID_PREFIX + id);
        if (authorization.getToken(OAuth2AuthorizationCode.class) != null) {
            redisTemplate.delete(REDIS_OAUTH2_CD_PREFIX + authorization.getToken(OAuth2AuthorizationCode.class).getToken().getTokenValue());
        }
        if (authorization.getAccessToken() != null) {
            redisTemplate.delete(REDIS_OAUTH2_AT_PREFIX + authorization.getAccessToken().getToken().getTokenValue());
        }
        if (authorization.getRefreshToken() != null) {
            redisTemplate.delete(REDIS_OAUTH2_RT_PREFIX + authorization.getRefreshToken().getToken().getTokenValue());
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        // 根据ID从Redis获取OAuth2Authorization对象
        return (OAuth2Authorization) redisTemplate.opsForValue().get(REDIS_OAUTH2_ID_PREFIX + id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        if (StrUtil.isBlank(token) || tokenType == null) {
            return null;
        }
        // 处理Authorization Code类型
        if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            // 先获取ID
            String id = (String) redisTemplate.opsForValue().get(REDIS_OAUTH2_CD_PREFIX + token);
            if (StrUtil.isNotBlank(id)) {
                // 根据ID获取OAuth2Authorization对象
                OAuth2Authorization authorization = findById(id);
                // 校验Authorization Code是否匹配
                if (authorization != null
                        && authorization.getToken(OAuth2AuthorizationCode.class) != null
                        && token.equals(authorization.getToken(OAuth2AuthorizationCode.class).getToken().getTokenValue())) {
                    return authorization;
                }
                // 删除无效的缓存
                redisTemplate.delete(REDIS_OAUTH2_CD_PREFIX + token);
            }
            return null;
        } else if (OAuth2TokenType.ACCESS_TOKEN == tokenType) { // 根据Token类型从Redis获取对应的OAuth2Authorization对象
            // 先获取ID
            String id = (String) redisTemplate.opsForValue().get(REDIS_OAUTH2_AT_PREFIX + token);
            if (StrUtil.isNotBlank(id)) {
                // 根据ID获取OAuth2Authorization对象
                OAuth2Authorization authorization = findById(id);
                // 校验Access Token是否匹配
                if (authorization != null
                        && token.equals(authorization.getAccessToken().getToken().getTokenValue())) {
                    return authorization;
                }
                // 删除无效的缓存
                redisTemplate.delete(REDIS_OAUTH2_AT_PREFIX + token);
            }
        } else if (OAuth2TokenType.REFRESH_TOKEN == tokenType) {
            String id = (String) redisTemplate.opsForValue().get(REDIS_OAUTH2_RT_PREFIX + token);
            if (StrUtil.isNotBlank(id)) {
                OAuth2Authorization authorization = findById(id);
                if (authorization != null
                        && token.equals(authorization.getRefreshToken().getToken().getTokenValue())) {
                    return authorization;
                }
                // 删除无效的缓存
                redisTemplate.delete(REDIS_OAUTH2_RT_PREFIX + token);
            }
        }
        return null;
    }
}
