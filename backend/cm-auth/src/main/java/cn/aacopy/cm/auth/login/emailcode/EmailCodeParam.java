package cn.aacopy.cm.auth.login.emailcode;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cmyang
 * @date 2026/3/3
 */
@Data
public class EmailCodeParam {
    @NotNull
    private Long authGroupId; // 认证组ID
    @NotBlank
    private String email; // 邮箱
    @NotBlank
    private String code; // 验证码
}
