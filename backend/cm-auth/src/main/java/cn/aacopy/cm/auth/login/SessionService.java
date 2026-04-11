package cn.aacopy.cm.auth.login;

import cn.aacopy.cm.auth.common.ServiceConstant;
import cn.aacopy.cm.auth.common.ServiceErrorCode;
import cn.aacopy.cm.auth.dao.AppDao;
import cn.aacopy.cm.auth.pojo.entity.AppDO;
import cn.aacopy.cm.starter.web.exception.ServiceException;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * 用户会话服务
 * @author cmyang
 * @date 2026/3/4
 */
@Service
public class SessionService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AppDao appDao;
    @Resource
    private Environment environment;

    /**
     * 获取会话信息
     * @param request 请求对象
     * @param response 获取响应
     * @return 会话信息
     */
    public SessionInfo getSession(HttpServletRequest request, HttpServletResponse response) {
        String clientId = ServletUtil.getParamMap(request).get("client_id");
        if (StrUtil.isBlank(clientId)) {
            throw new ServiceException(ServiceErrorCode.APP_NOT_FOUND);
        }
        // 获取应用信息
        AppDO appDO = appDao.getByClientId(clientId);
        String sessionId = getSessionId(request);
        if (StrUtil.isNotBlank(sessionId)) {
            String sessionRedisKey = ServiceConstant.REDIS_SESSION_PREFIX + sessionId;
            String sessionStr = (String) stringRedisTemplate.opsForHash().get(sessionRedisKey, appDO.getAuthGroupId().toString());
            if (StrUtil.isBlank(sessionStr)) {
                return null;
            }
            SessionInfo sessionInfo = JSONUtil.toBean(sessionStr, SessionInfo.class);
            // 判断是否需要刷新会话
            if (sessionInfo.getRefreshTime() < Instant.now().getEpochSecond()) {
                // 刷新会话
                // 获取登录时的应用
                AppDO loginApp = appDao.getById(sessionInfo.getAppId());
                // 计算需要刷新Session的时间
                long refreshTime = Instant.now().getEpochSecond() + loginApp.getAccessExpires();
                sessionInfo.setRefreshTime(refreshTime);

                String newSessionId = IdUtil.fastSimpleUUID();
                String newSessionRedisKey = ServiceConstant.REDIS_SESSION_PREFIX + newSessionId;
                Long expire = loginApp.getRefreshExpires();
                // 如果当前会话的过期时间小于鉴权组的刷新过期时间，则更新会话的过期时间
                Long redisExpire = stringRedisTemplate.getExpire(sessionRedisKey);
                if (expire < redisExpire) {
                    expire = redisExpire;
                }
                // 设置缓冲时间自动过期
                stringRedisTemplate.expire(sessionRedisKey, 60, TimeUnit.SECONDS);
                // 设置新会话
                stringRedisTemplate.opsForHash().put(newSessionRedisKey, sessionInfo.getAuthGroupId().toString(), JSONUtil.toJsonStr(sessionInfo));
                stringRedisTemplate.expire(newSessionRedisKey, expire, TimeUnit.SECONDS);
                // 设置Cookie
                createCookie(response, newSessionId, expire);
            }
            return sessionInfo;
        }
        return null;
    }

    /**
     * 创建会话
     * @param request 请求对象
     * @param response 响应对象
     * @param authentication 认证对象
     */
    public void createSession(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String clientId = ServletUtil.getParamMap(request).get("client_id");
        if (StrUtil.isBlank(clientId)) {
            throw new ServiceException(ServiceErrorCode.APP_NOT_FOUND);
        }
        AuthInfo authInfo = (AuthInfo) authentication.getPrincipal();
        AppDO appDO = appDao.getByClientId(clientId);
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setAuthGroupId(authInfo.getAuthGroupId());
        sessionInfo.setAppId(appDO.getId());
        sessionInfo.setUserId(authInfo.getUserId());
        // 计算需要刷新Session的时间
        long refreshTime = Instant.now().getEpochSecond() + appDO.getAccessExpires();
        sessionInfo.setRefreshTime(refreshTime);

        // 新SessionId
        String newSessionId = IdUtil.fastSimpleUUID();
        String newSessionRedisKey = ServiceConstant.REDIS_SESSION_PREFIX + newSessionId;
        Long expire = appDO.getRefreshExpires();
        // 旧SessionId
        String sessionId = getSessionId(request);
        if (StrUtil.isNotBlank(sessionId)) {
            String sessionRedisKey = ServiceConstant.REDIS_SESSION_PREFIX + sessionId;
            if (stringRedisTemplate.hasKey(sessionRedisKey)) {
                // 如果当前会话的过期时间小于鉴权组的刷新过期时间，则更新会话的过期时间
                Long redisExpire = stringRedisTemplate.getExpire(sessionRedisKey);
                if (expire < redisExpire) {
                    expire = redisExpire;
                }
                // 设置缓冲时间自动过期
                stringRedisTemplate.expire(sessionRedisKey, 60, TimeUnit.SECONDS);
            }
        }
        stringRedisTemplate.opsForHash().put(newSessionRedisKey, authInfo.getAuthGroupId().toString(), JSONUtil.toJsonStr(sessionInfo));
        stringRedisTemplate.expire(newSessionRedisKey, expire, TimeUnit.SECONDS);

        // 设置Cookie
        createCookie(response, newSessionId, expire);
    }

    private void createCookie(HttpServletResponse response, String sessionId, Long expire) {
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(ServiceConstant.SESSION_KEY, sessionId)
                .httpOnly(true)
                .path("/")
                .maxAge(expire);
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0 && StrUtil.equals(activeProfiles[0], "dev")) {
            responseCookieBuilder.secure(false);
            responseCookieBuilder.sameSite("Lax");
        } else {
            responseCookieBuilder.secure(true);
            responseCookieBuilder.sameSite("None");
        }
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookieBuilder.build().toString());
    }

    private String getSessionId(HttpServletRequest request) {
        Cookie cookie = ServletUtil.getCookie(request, ServiceConstant.SESSION_KEY);
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }
}
