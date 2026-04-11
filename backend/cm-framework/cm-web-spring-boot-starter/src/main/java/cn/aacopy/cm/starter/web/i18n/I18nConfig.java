package cn.aacopy.cm.starter.web.i18n;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 国际化配置类
 * @author cmyang
 * @date 2026/2/23
 */
@AutoConfiguration
public class I18nConfig {

    @Bean
    @ConditionalOnMissingBean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.addBasenames("classpath:i18n/common", "classpath:i18n/messages");
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(-1); // 生产缓存永久
        source.setUseCodeAsDefaultMessage(true); // 如果找不到 key，是否直接返回 key 本身作为消息
        source.setFallbackToSystemLocale(false); // 是否回退到系统区域设置，建议设为 false 以使用默认文件
        return source;
    }
}
