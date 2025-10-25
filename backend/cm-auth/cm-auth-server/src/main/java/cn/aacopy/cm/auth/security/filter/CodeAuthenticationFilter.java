package cn.aacopy.cm.auth.security.filter;

import cn.aacopy.cm.auth.security.token.CodeAuthenticationToken;
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
 * @author cmyang
 * @date 2025/10/24
 */
@Component
public class CodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 拦截验证码登录地址
     */
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/v1/login/code", "POST");

    public CodeAuthenticationFilter(AuthenticationManager authenticationManager,
                                    AuthenticationSuccessHandler authenticationSuccessHandler,
                                    AuthenticationFailureHandler authenticationFailureHandler) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String poolId = request.getParameter("poolId");
        poolId = (poolId != null) ? poolId.trim() : "";
        String username = request.getParameter("username");
        username = (username != null) ? username.trim() : "";
        String code = request.getParameter("code");
        code = (code != null) ? code : "";
        CodeAuthenticationToken unauthenticatedToken = CodeAuthenticationToken.unauthenticated(username);

        unauthenticatedToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(unauthenticatedToken);
    }
}
