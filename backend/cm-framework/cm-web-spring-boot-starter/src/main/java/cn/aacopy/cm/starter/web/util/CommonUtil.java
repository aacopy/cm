package cn.aacopy.cm.starter.web.util;

/**
 * @author cmyang
 * @date 2026/2/23
 */
public class CommonUtil {

    /**
     * 判断字符串是否为 "Y"
     */
    public static boolean isY(String str) {
        return "Y".equals(str);
    }

    /**
    * 判断字符串是否不为 "Y"
    */
    public static boolean notY(String str) {
        return !isY(str);
    }

    /**
     * 判断字符串是否为 "N"
     */
    public static boolean isN(String str) {
        return "N".equals(str);
    }

    /**
     * 判断字符串是否不为 "N"
     */
    public static boolean notN(String str) {
        return !isN(str);
    }

}
