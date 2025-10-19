package cn.aacopy.cm.auth.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户详情服务
 * @author cmyang
 * @create 2025/10/8 20:32
 */
@Component
public class JdbcUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return User.withUsername("cmyang")
                .password("123456")
                .authorities("ROLE_ADMIN")
                .build();
    }
}
