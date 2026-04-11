package cn.aacopy.cm.auth.util;

import cn.aacopy.cm.auth.common.ServiceErrorCode;
import cn.aacopy.cm.starter.web.exception.CommonErrorCode;
import cn.aacopy.cm.starter.web.exception.ServiceException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * 校验工具类
 * @author cmyang
 * @date 2026/3/3
 */
public class ValidationUtil {

    /**
     * 参数校验
     */
    public static void validateParam(Object validateObj) {
        javax.validation.Validator validator = SpringUtil.getBean(javax.validation.Validator.class);
        Set<ConstraintViolation<Object>> violations = validator.validate(validateObj);
        if (CollUtil.isNotEmpty(violations)) {
            ConstraintViolation<Object> violation = violations.iterator().next();
            String name = violation.getPropertyPath().iterator().next().getName();
            throw new ServiceException(CommonErrorCode.INVALID_PARAM, name + " " + violation.getMessage());
        }
    }

    /**
     * 邮箱格式校验
     */
    public static void validateEmail(String email) {
        if (StrUtil.isNotBlank(email)) {
            boolean isEmail = Validator.isEmail(email);
            if (!isEmail) {
                throw new ServiceException(ServiceErrorCode.EMAIL_FORMAT_ERROR);
            }
        }
    }
}
