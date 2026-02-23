package cn.aacopy.cm.starter.web.exception;

/**
 * 未认证异常
 * @author cmyang
 * @date 2026/2/17
 */
public class UnauthorizedException extends ServiceException {

    public UnauthorizedException() {
        super(CommonErrorCode.UNAUTHORIZED);
    }
}
