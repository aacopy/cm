package cn.aacopy.cm.auth.login.emailcode;

import cn.aacopy.cm.auth.login.AuthInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;

/**
 * @author cmyang
 * @date 2026/3/3
 */
public class EmailCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    /**
     * 创建未认证的AuthenticationToken
     */
    private EmailCodeAuthenticationToken(EmailCodeParam emailCodeParam) {
        super(null);
        this.principal = emailCodeParam;
        super.setAuthenticated(false);
    }

    /**
     * 创建已认证的AuthenticationToken
     */
    private EmailCodeAuthenticationToken(AuthInfo principal) {
        super(new ArrayList<>());
        this.principal = principal;
        super.setAuthenticated(true);
    }

    /**
     * 构建未认证的AuthenticationToken
     */
    public static EmailCodeAuthenticationToken unauthenticated(EmailCodeParam emailCodeParam) {
        return new EmailCodeAuthenticationToken(emailCodeParam);
    }

    /**
     * 构建已认证的AuthenticationToken
     */
    public static EmailCodeAuthenticationToken authenticated(AuthInfo authInfo) {
        return new EmailCodeAuthenticationToken(authInfo);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
