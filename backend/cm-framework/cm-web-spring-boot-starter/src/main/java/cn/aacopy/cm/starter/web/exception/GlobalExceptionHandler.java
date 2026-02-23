package cn.aacopy.cm.starter.web.exception;

import cn.aacopy.cm.starter.web.pojo.Result;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author cmyang
 * @date 2026/2/16
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<?> handleServiceException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return Result.failure(CommonErrorCode.INVALID_PARAM.getCode(), e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result<?> handleUnauthorized(UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public Result<?> handleForbidden(ForbiddenException e) {
        log.error(e.getMessage(), e);
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = null;
        FieldError fieldError = e.getFieldError();
        if (fieldError != null) {
            message = fieldError.getField() + e.getFieldError().getDefaultMessage();
        }
        if (StrUtil.isBlank(message)) {
            message = "Invalid param";
        }
        log.error(message, e);
        return Result.failure(CommonErrorCode.INVALID_PARAM.getCode(), message);
    }

    @ExceptionHandler(value = Exception.class)
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        // 不要把系统异常详细信息暴露到前端
        return Result.failure(-1, "Internal Server Error");
    }
}
