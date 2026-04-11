package cn.aacopy.cm.auth.login;

import cn.aacopy.cm.starter.web.exception.ServiceException;
import cn.aacopy.cm.starter.web.pojo.Result;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cmyang
 * @date 2026/2/15
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private SessionService sessionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            sessionService.createSession(request, response, authentication);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONUtil.toJsonStr(Result.success()));
        } catch (ServiceException e) {
            ExceptionEntryPoint.commenceServiceException(response, e);
        } catch (Exception e) {
            ExceptionEntryPoint.commenceException(response, e);
        }
    }

}
