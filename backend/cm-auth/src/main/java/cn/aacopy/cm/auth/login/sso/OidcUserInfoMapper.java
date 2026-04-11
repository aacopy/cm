package cn.aacopy.cm.auth.login.sso;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author cmyang
 * @date 2026/2/15
 */
@Component
public class OidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {

        OAuth2Authorization authorization = context.getAuthorization();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authorization.getPrincipalName());
        return new OidcUserInfo(claims);
    }
}
