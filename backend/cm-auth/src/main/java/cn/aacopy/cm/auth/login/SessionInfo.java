package cn.aacopy.cm.auth.login;

import lombok.Data;

/**
 * 认证会话信息
 * @author cmyang
 * @date 2026/3/3
 */
@Data
public class SessionInfo {
    private Long authGroupId; // 认证组ID
    private Long appId; // 应用ID
    private Long userId; // 用户ID
    private Long refreshTime; // 刷新时间戳(秒)
}
