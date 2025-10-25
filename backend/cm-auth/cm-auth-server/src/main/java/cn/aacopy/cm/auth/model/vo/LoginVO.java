package cn.aacopy.cm.auth.model.vo;

import lombok.Data;

import java.time.Instant;

/**
 * 登录返回对象
 * @author cmyang
 * @date 2025/10/24
 */
@Data
public class LoginVO {
    private String username;
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private Instant expiresIn;
}
