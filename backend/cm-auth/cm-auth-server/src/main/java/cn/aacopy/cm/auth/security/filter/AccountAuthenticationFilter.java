package cn.aacopy.cm.auth.security.filter;

import cn.aacopy.cm.auth.security.token.AccountAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 账号密码认证过滤器
 * @author cmyang
 * @date 2025/10/24
 */
@Component
public class AccountAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 拦截账号密码登录地址
     */
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/v1/login/account", "POST");

    public AccountAuthenticationFilter(AuthenticationManager authenticationManager,
                                       AuthenticationSuccessHandler authenticationSuccessHandler,
                                       AuthenticationFailureHandler authenticationFailureHandler) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 获取用户池、账号、密码、图形验证码
        String poolId = request.getParameter("poolId");
        poolId = (poolId != null) ? poolId.trim() : "";
        String username = request.getParameter("username");
        username = (username != null) ? username.trim() : "";
        String password = request.getParameter("password");
        password = (password != null) ? password : "";
        String captcha = request.getParameter("captcha");

        // 验证图形验证码 TODO

        // 创建待验证的对象
        AccountAuthenticationToken authenticationToken = new AccountAuthenticationToken(username, password, poolId);
        // 添加额外信息
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
