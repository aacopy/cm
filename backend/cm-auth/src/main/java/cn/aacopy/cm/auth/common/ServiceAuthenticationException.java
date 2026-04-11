package cn.aacopy.cm.auth.common;

import cn.aacopy.cm.starter.web.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * @author cmyang
 * @date 2026/2/16
 */
@Getter
public class ServiceAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;
    private final Object[] args;

    public ServiceAuthenticationException(ErrorCode errorCode, Object... args) {
        super(String.valueOf(errorCode.getCode()));
        this.errorCode = errorCode;
        this.args = args;
    }
}
