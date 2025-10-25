package cn.aacopy.cm.auth.security.provider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 账号密码认证提供者
 * @author cmyang
 * @date 2025/10/24
 */
@Component
public class AccountAuthenticationProvider extends DaoAuthenticationProvider {

    public AccountAuthenticationProvider(UserDetailsService userDetailsService) {
        setUserDetailsService(userDetailsService);
    }
}
