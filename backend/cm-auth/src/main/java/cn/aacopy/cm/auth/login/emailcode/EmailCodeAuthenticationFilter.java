package cn.aacopy.cm.auth.login.emailcode;

import cn.aacopy.cm.auth.util.ValidationUtil;
import cn.hutool.extra.servlet.ServletUtil;
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
 * @date 2026/3/3
 */
@Component
public class EmailCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/login/emailCode", "POST");

    public EmailCodeAuthenticationFilter(AuthenticationManager authenticationManager,
                                         AuthenticationSuccessHandler successHandler,
                                         AuthenticationFailureHandler failureHandler) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        EmailCodeParam emailCodeParam = ServletUtil.toBean(request, EmailCodeParam.class, false);
        ValidationUtil.validateParam(emailCodeParam);
        ValidationUtil.validateEmail(emailCodeParam.getEmail());
        EmailCodeAuthenticationToken unauthenticatedToken = EmailCodeAuthenticationToken.unauthenticated(emailCodeParam);
        unauthenticatedToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(unauthenticatedToken);
    }
}
