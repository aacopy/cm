package cn.aacopy.cm.auth.common;

/**
 * @author cmyang
 * @date 2026/2/28
 */
public class ServiceConstant {
    // 认证会话 Cookie Key
    public static final String SESSION_KEY = "AUTH_SESSION";

    // *********************Redis Keys*********************
    // 认证会话用户信息前缀
    public static final String REDIS_SESSION_PREFIX = "AUTH:SESSION:";
    // Token ID Redis键前缀
    public static final String REDIS_TOKEN_ID_PREFIX = "AUTH:TOKEN:ID:";
    // Authorization State Redis键前缀
    public static final String REDIS_TOKEN_ST_PREFIX = "AUTH:TOKEN:ST:";
    // Authorization Code Redis键前缀
    public static final String REDIS_TOKEN_CD_PREFIX = "AUTH:TOKEN:CD:";
}
