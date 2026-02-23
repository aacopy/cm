package cn.aacopy.cm.starter.web.exception;

import lombok.Getter;

/**
 * 服务异常
 * @author cmyang
 * @date 2026/2/16
 */
@Getter
public class ServiceException extends RuntimeException {
    private final Integer code;

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
