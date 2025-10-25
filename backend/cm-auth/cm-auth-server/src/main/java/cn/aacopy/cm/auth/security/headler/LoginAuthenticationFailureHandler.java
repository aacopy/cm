package cn.aacopy.cm.auth.security.headler;

import cn.aacopy.cm.auth.common.Result;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证失败处理器
 * @author cmyang
 * @date 2025/10/24
 */
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<Object> failureResult = Result.failure(401, "Authentication Failed: " + exception.getMessage());
        response.getWriter().write(JSONUtil.toJsonStr(failureResult, JSONConfig.create().setIgnoreNullValue(false)));
    }
}
