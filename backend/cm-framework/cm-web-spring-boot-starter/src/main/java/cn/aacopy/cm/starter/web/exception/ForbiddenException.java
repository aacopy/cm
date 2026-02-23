package cn.aacopy.cm.starter.web.exception;

/**
 * 未授权异常
 * @author cmyang
 * @date 2026/2/17
 */
public class ForbiddenException extends ServiceException {

    public ForbiddenException() {
        super(CommonErrorCode.FORBIDDEN);
    }
}
