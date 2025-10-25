package cn.aacopy.cm.auth.security.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * OIDC认证单点登录过滤器
 * @author cmyang
 * @date 2025/10/24
 */
@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie accessToken = WebUtils.getCookie(request, "accessToken");
        if (accessToken != null) {
            String accessTokenValue = accessToken.getValue();
            System.out.println(accessTokenValue);
            // 验证Token是否有效 TODO
            Jwt decode = jwtDecoder.decode(accessTokenValue);
            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(decode.getSubject(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
