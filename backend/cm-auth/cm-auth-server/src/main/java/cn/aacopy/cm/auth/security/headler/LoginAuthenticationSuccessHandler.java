package cn.aacopy.cm.auth.security.headler;

import cn.aacopy.cm.auth.common.Result;
import cn.aacopy.cm.auth.model.vo.LoginVO;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理器
 * @author cmyang
 * @date 2025/10/24
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final AuthorizationGrantType ACCOUNT_PASSWORD_GRANT = new AuthorizationGrantType("account_password");
    private static final OAuth2TokenType ID_TOKEN = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    @Resource
    private OAuth2TokenGenerator<?> oauth2TokenGenerator;
    @Resource
    private RegisteredClientRepository registeredClientRepository;
    @Resource
    private OAuth2AuthorizationService authorizationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取客户端信息
        RegisteredClient registeredClient = registeredClientRepository.findByClientId("qauth");

        // 生成ACCESS_TOKEN令牌
        DefaultOAuth2TokenContext accessTokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizationGrantType(ACCOUNT_PASSWORD_GRANT)
                .authorizedScopes(registeredClient.getScopes())
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .build();
        Jwt accessJwtToken = (Jwt) oauth2TokenGenerator.generate(accessTokenContext);
        if (accessJwtToken == null) {
            throw new IllegalStateException("无法生成accessToken令牌");
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                accessJwtToken.getTokenValue(),
                accessJwtToken.getIssuedAt(),
                accessJwtToken.getExpiresAt(),
                registeredClient.getScopes()
        );

        // 生成REFRESH_TOKEN令牌
        DefaultOAuth2TokenContext refreshTokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizationGrantType(ACCOUNT_PASSWORD_GRANT)
                .authorizedScopes(registeredClient.getScopes())
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .build();
        OAuth2RefreshToken refreshToken = (OAuth2RefreshToken) oauth2TokenGenerator.generate(refreshTokenContext);
        if (refreshToken == null) {
            throw new IllegalStateException("无法生成refreshToken令牌");
        }

        // 生成ID_TOKEN令牌
        DefaultOAuth2TokenContext idTokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizationGrantType(ACCOUNT_PASSWORD_GRANT)
                .authorizedScopes(registeredClient.getScopes())
                .tokenType(ID_TOKEN)
                .build();
        Jwt idJwtToken = (Jwt) oauth2TokenGenerator.generate(idTokenContext);
        if (idJwtToken == null) {
            throw new IllegalStateException("无法生成idToken令牌");
        }
        OidcIdToken idToken = new OidcIdToken(
                idJwtToken.getTokenValue(),
                idJwtToken.getIssuedAt(),
                idJwtToken.getExpiresAt(),
                idJwtToken.getClaims()
        );

        // 构建 OAuth2Authorization 并保存
        OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(authentication.getName())
                .authorizedScopes(registeredClient.getScopes())
                .authorizationGrantType(ACCOUNT_PASSWORD_GRANT)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .token(idToken)
                .build();
        authorizationService.save(authorization); // 保存授权信息

        LoginVO loginVO = new LoginVO();
        loginVO.setUsername(authentication.getName());
        loginVO.setAccessToken(accessToken.getTokenValue());
        loginVO.setRefreshToken(refreshToken.getTokenValue());
        loginVO.setIdToken(idToken.getTokenValue());
        loginVO.setExpiresIn(accessToken.getExpiresAt());

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(Result.success(loginVO)));
    }
}
