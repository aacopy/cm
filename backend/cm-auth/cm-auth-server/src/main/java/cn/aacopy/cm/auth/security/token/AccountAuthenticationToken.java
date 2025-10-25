package cn.aacopy.cm.auth.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 账户密码认证令牌
 * @author cmyang
 * @date 2025/10/24
 */
public class AccountAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String poolId; // 用户池ID

    /**
     * 任何希望创建AccountAuthenticationToken代码都可以安全地使用此构造函数，因为isAuthenticated()将返回false 。
     * @param principal 账号
     * @param credentials 密码
     * @param poolId 用户池
     */
    public AccountAuthenticationToken(Object principal, Object credentials, String poolId) {
        super(principal, credentials);
        this.poolId = poolId;
        setAuthenticated(false);
    }

    /**
     * 认证成功后调用此构造函数创建已认证的AccountAuthenticationToken
     * @param principal 账号
     * @param credentials 密码
     * @param poolId 用户池
     * @param authorities 权限列表
     */
    public AccountAuthenticationToken(Object principal, Object credentials, String poolId, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.poolId = poolId;
    }
}
