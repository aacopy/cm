package cn.aacopy.cm.auth.login.sso;

import cn.aacopy.cm.auth.common.ServiceErrorCode;
import cn.aacopy.cm.auth.config.AuthProperties;
import cn.aacopy.cm.auth.dao.AppDao;
import cn.aacopy.cm.auth.login.AuthInfo;
import cn.aacopy.cm.auth.login.ExceptionEntryPoint;
import cn.aacopy.cm.auth.login.SessionInfo;
import cn.aacopy.cm.auth.login.SessionService;
import cn.aacopy.cm.auth.login.emailcode.EmailCodeAuthenticationToken;
import cn.aacopy.cm.starter.web.exception.ServiceException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单点登录服务
 * @author cmyang
 * @date 2026/2/15
 */
@Service
public class SsoService {

    @Resource
    private SessionService sessionService;
    @Resource
    private AuthProperties authProperties;

    public boolean authorizationRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String clientId = ServletUtil.getParamMap(request).get("client_id");
            if (StrUtil.isBlank(clientId)) {
                throw new ServiceException(ServiceErrorCode.APP_NOT_FOUND);
            }
            // 获取当前鉴权组的会话用户信息
            SessionInfo sessionInfo = sessionService.getSession(request, response);
            if (sessionInfo == null) {
                // 没有登录，重定向到登录页
                String requestUri = authProperties.getDomain() + "/api/oauth2/authorize?" + request.getQueryString();
                String requestUrl = URLUtil.encodeAll(requestUri);
                String loginPageUrl = StrUtil.format(authProperties.getDomain() + "/login?clientId={}&callback={}", clientId, requestUrl);
                response.sendRedirect(loginPageUrl);
                return false;
            }
            // 已登录，设置认证信息并继续授权流程
            AuthInfo authInfo = new AuthInfo();
            authInfo.setAuthGroupId(sessionInfo.getAuthGroupId());
            authInfo.setUserId(sessionInfo.getUserId());
            EmailCodeAuthenticationToken authenticationToken = EmailCodeAuthenticationToken.authenticated(authInfo);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return true;
        } catch (ServiceException e) {
            ExceptionEntryPoint.commenceServiceException(response, e);
        } catch (Exception e) {
            ExceptionEntryPoint.commenceException(response, e);
        }
        return false;
    }
}
