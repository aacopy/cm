package cn.aacopy.cm.auth.login;

import cn.aacopy.cm.auth.common.ServiceAuthenticationException;
import cn.aacopy.cm.starter.web.exception.CommonErrorCode;
import cn.aacopy.cm.starter.web.pojo.Result;
import cn.aacopy.cm.starter.web.util.I18nUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 * @author cmyang
 * @date 2026/2/15
 */
@Component
@Slf4j
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error(exception.getMessage(), exception);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        Result<Object> failureResult;
        int code = CommonErrorCode.SERVER_ERROR.getCode();
        if (exception instanceof ServiceAuthenticationException) {
            ServiceAuthenticationException serviceException = (ServiceAuthenticationException) exception;
            String message = I18nUtil.translate(serviceException.getErrorCode(), serviceException.getArgs());
            code = serviceException.getErrorCode().getCode();
            failureResult = Result.failure(code, message);
        } else {
            failureResult = Result.failure(code, exception.getMessage());
        }
        response.getWriter().write(JSONUtil.toJsonStr(failureResult, JSONConfig.create().setIgnoreNullValue(false)));
    }
}
