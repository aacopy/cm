package cn.aacopy.cm.auth.login;

import cn.aacopy.cm.auth.common.ServiceConstant;
import cn.aacopy.cm.starter.web.exception.CommonErrorCode;
import cn.aacopy.cm.starter.web.exception.ServiceException;
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
 * OAuth2AuthorizationService的Redis实现
 * @author cmyang
 * @date 2026/3/4
 */
@Component
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    @Resource(name = "javaValueRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(OAuth2Authorization authorization) {
        if (authorization == null) {
            throw new OAuth2AuthorizationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN));
        }
        String id = authorization.getId();

        // 处理State模式
        String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
        if (StrUtil.isNotBlank(state)) {
            redisTemplate.opsForValue().set(ServiceConstant.REDIS_TOKEN_ID_PREFIX + id, authorization, 10, TimeUnit.MINUTES);
            // Key State , Value OAuth2Authorization ID
            redisTemplate.opsForValue().set(ServiceConstant.REDIS_TOKEN_ST_PREFIX + state, id, 10, TimeUnit.MINUTES);
            return;
        }

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        // 处理授权码模式
        if (authorizationCode != null) {
            long cdExpiresIn = ChronoUnit.SECONDS.between(authorizationCode.getToken().getIssuedAt(), authorizationCode.getToken().getExpiresAt());
            // Key ID , Value OAuth2Authorization Java对象
            redisTemplate.opsForValue().set(ServiceConstant.REDIS_TOKEN_ID_PREFIX + id, authorization, cdExpiresIn, TimeUnit.SECONDS);
            // Key Authorization Code , Value OAuth2Authorization ID
            redisTemplate.opsForValue().set(ServiceConstant.REDIS_TOKEN_CD_PREFIX + authorizationCode.getToken().getTokenValue(), id, cdExpiresIn, TimeUnit.SECONDS);
            return;
        }

        // 处理Access Token和Refresh Token
        throw new ServiceException(CommonErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        if (authorization == null) {
            return;
        }
        // 删除所有相关缓存
        String id = authorization.getId();
        redisTemplate.delete(ServiceConstant.REDIS_TOKEN_ID_PREFIX + id);
        if (StrUtil.isNotBlank(authorization.getAttribute(OAuth2ParameterNames.STATE))) {
            redisTemplate.delete(ServiceConstant.REDIS_TOKEN_ST_PREFIX + authorization.getAttribute(OAuth2ParameterNames.STATE));
        }
        if (authorization.getToken(OAuth2AuthorizationCode.class) != null) {
            redisTemplate.delete(ServiceConstant.REDIS_TOKEN_CD_PREFIX + authorization.getToken(OAuth2AuthorizationCode.class).getToken().getTokenValue());
        }
        if (authorization.getAccessToken() != null) {
            throw new ServiceException(CommonErrorCode.UNSUPPORTED_OPERATION);
        }
        if (authorization.getRefreshToken() != null) {
            throw new ServiceException(CommonErrorCode.UNSUPPORTED_OPERATION);
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        // 根据ID从Redis获取OAuth2Authorization对象
        return (OAuth2Authorization) redisTemplate.opsForValue().get(ServiceConstant.REDIS_TOKEN_ID_PREFIX + id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        if (StrUtil.isBlank(token) || tokenType == null) {
            return null;
        }
        if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            // 处理State类型
            String id = (String) redisTemplate.opsForValue().get(ServiceConstant.REDIS_TOKEN_ST_PREFIX + token);
            if (StrUtil.isNotBlank(id)) {
                OAuth2Authorization authorization = findById(id);
                if (authorization != null
                        && token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE))) {
                    return authorization;
                }
                // 删除无效的缓存
                redisTemplate.delete(ServiceConstant.REDIS_TOKEN_ST_PREFIX + token);
            }
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) { // 处理Authorization Code类型
            // 先获取ID
            String id = (String) redisTemplate.opsForValue().get(ServiceConstant.REDIS_TOKEN_CD_PREFIX + token);
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
                redisTemplate.delete(ServiceConstant.REDIS_TOKEN_CD_PREFIX + token);
            }
        } else {
            throw new ServiceException(CommonErrorCode.UNSUPPORTED_OPERATION);
        }
        return null;
    }
}
