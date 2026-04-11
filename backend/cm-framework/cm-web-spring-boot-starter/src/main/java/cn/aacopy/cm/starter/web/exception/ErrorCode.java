package cn.aacopy.cm.starter.web.exception;

/**
 * 统一错误码字段
 * @author cmyang
 * @date 2026/2/17
 */
public interface ErrorCode {

    /**
     * 获取错误码
     * @return 错误码
     */
    int getCode();

    /**
     * 国际化的错误提示
     * @return 国际化key
     */
    String getI18nKey();
}
