package cn.aacopy.cm.auth.common;

import cn.aacopy.cm.starter.web.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cmyang
 * @date 2026/2/23
 */
@Getter
@AllArgsConstructor
public enum ServiceErrorCode implements ErrorCode {

    EMAIL_FORMAT_ERROR(102001, "auth.email_format_error"),
    ACCOUNT_DISABLED(102002, "auth.account_disabled"),
    VERIFICATION_CODE_ERROR(102003, "auth.verification_code_error"),
    APP_NOT_FOUND(102004, "auth.app_not_found"),
    APP_NOT_ENABLED(102005, "auth.app_not_enabled"),
    USER_NOT_FOUND(102006, "auth.user_not_found"),
    USER_DISABLED(102007, "auth.user_disabled"),
    AUTH_GROUP_NOT_FOUND(102008, "auth.auth_group_not_found"),
    ;

    private final int code;
    private final String i18nKey;
}
