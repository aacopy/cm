package cn.aacopy.cm.auth.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author cmyang
 * @date 2025/10/24
 */
public class CodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    /**
     * 创建未认证的CodeAuthenticationToken
     * @param principal
     */
    public CodeAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        super.setAuthenticated(false);
    }

    /**
     * 创建已认证的CodeAuthenticationToken
     * @param principal 用户标识，如手机号、邮箱等
     */
    public CodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    /**
     * 创建未认证对象
     * @param principal
     * @return
     */
    public static CodeAuthenticationToken unauthenticated(Object principal) {
        return new CodeAuthenticationToken(principal);
    }

    /**
     * 创建已认证对象
     * @param principal
     * @return
     */
    public static CodeAuthenticationToken authenticated(Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new CodeAuthenticationToken(principal, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }
}
