package cn.aacopy.cm.auth.login.sso;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单点登录认证过滤器
 * @author cmyang
 * @date 2026/2/15
 */
public class SsoFilter extends OncePerRequestFilter {

    private final SsoService ssoService;

    private final AntPathRequestMatcher SSO_REQUEST_MATCHER = new AntPathRequestMatcher("/oauth2/authorize");

    public SsoFilter(SsoService ssoService) {
        this.ssoService = ssoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.SSO_REQUEST_MATCHER.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        boolean success = ssoService.authorizationRequest(request, response);
        if (success) {
            filterChain.doFilter(request, response);
        }
    }
}
