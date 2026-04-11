package cn.aacopy.cm.starter.web.exception;

import cn.aacopy.cm.starter.web.pojo.Result;
import cn.aacopy.cm.starter.web.util.I18nUtil;
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
        String message = I18nUtil.translate(e.getErrorCode(), e.getArgs());
        log.error(message, e);
        return Result.failure(e.getErrorCode().getCode(), message);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<?> handleServiceException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return Result.failure(CommonErrorCode.INVALID_PARAM.getCode(), e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result<?> handleUnauthorized(UnauthorizedException e) {
        String message = I18nUtil.translate(e.getErrorCode(), e.getArgs());
        log.error(message, e);
        return Result.failure(e.getErrorCode().getCode(), message);
    }

    @ExceptionHandler(ForbiddenException.class)
    public Result<?> handleForbidden(ForbiddenException e) {
        String message = I18nUtil.translate(e.getErrorCode(), e.getArgs());
        log.error(message, e);
        return Result.failure(e.getErrorCode().getCode(), message);
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
        String message = I18nUtil.translate(CommonErrorCode.SERVER_ERROR);
        return Result.failure(CommonErrorCode.SERVER_ERROR.getCode(), message);
    }
}
