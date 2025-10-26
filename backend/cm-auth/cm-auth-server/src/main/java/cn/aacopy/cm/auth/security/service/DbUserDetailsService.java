package cn.aacopy.cm.auth.security.service;

import cn.aacopy.cm.auth.model.entity.UserDO;
import cn.aacopy.cm.auth.service.UserService;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 数据库用户详情服务
 * @author cmyang
 * @date 2025/10/24
 */
@Component
public class DbUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;

    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO userDO = userService.findAccount(username);
        if (userDO == null) {
            throw new BadCredentialsException(messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        return User.withUsername(userDO.getUsername())
                .password("{" + userDO.getAlgorithm() + "}" + userDO.getPassword())
                .authorities("ROLE_ADMIN")
                .build();
    }
}
