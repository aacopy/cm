package cn.aacopy.cm.starter.web.util;

import cn.aacopy.cm.starter.web.exception.ErrorCode;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 国际化工具类
 * @author cmyang
 * @date 2026/2/23
 */
public class I18nUtil {

    /**
     * 根据错误码，翻译成国际化的错误信息
     * @param errorCode 错误码
     * @return 国际化的错误信息
     */
    public static String translate(ErrorCode errorCode, Object... args) {
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorCode.getI18nKey(), args, errorCode.getI18nKey(), locale);
    }
}
