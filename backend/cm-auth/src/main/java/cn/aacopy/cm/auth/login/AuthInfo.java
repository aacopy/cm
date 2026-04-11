package cn.aacopy.cm.auth.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 认证信息
 * @author cmyang
 * @date 2026/3/3
 */
@Data
public class AuthInfo implements Serializable {
    private Long authGroupId; // 认证组ID
    private Long userId; // 用户ID
}
