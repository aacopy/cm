package cn.aacopy.cm.starter.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cmyang
 * @date 2026/2/16
 */
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "未授权"),

    SERVER_ERROR(100001, "系统异常"),
    INVALID_PARAM(100002, "参数错误"),
    ;

    private final int code;
    private final String message;
}
