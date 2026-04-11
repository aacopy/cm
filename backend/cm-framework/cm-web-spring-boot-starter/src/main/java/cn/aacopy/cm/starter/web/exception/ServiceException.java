package cn.aacopy.cm.starter.web.exception;

import lombok.Getter;

/**
 * 服务异常
 * @author cmyang
 * @date 2026/2/16
 */
@Getter
public class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] args;

    public ServiceException(ErrorCode errorCode, Object... args) {
        this.errorCode = errorCode;
        this.args = args;
    }
}
