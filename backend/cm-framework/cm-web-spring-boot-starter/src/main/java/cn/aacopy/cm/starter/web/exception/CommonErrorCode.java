package cn.aacopy.cm.starter.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用错误码枚举
 * @author cmyang
 * @date 2026/2/16
 */
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    UNAUTHORIZED(401, "common.unauthorized"),
    FORBIDDEN(403, "common.forbidden"),

    SERVER_ERROR(100001, "common.server_error"),
    INVALID_PARAM(100002, "common.invalid_param"),
    UNSUPPORTED_OPERATION(100003, "common.unsupported_operation")
    ;

    private final int code;
    private final String i18nKey;
}
