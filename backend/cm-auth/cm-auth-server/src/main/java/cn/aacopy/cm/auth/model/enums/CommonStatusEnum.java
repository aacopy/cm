package cn.aacopy.cm.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cmyang
 * @date 2025/10/26
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {

    ENABLED(1, "启用"),
    DISABLED(0, "禁用");

    private final Integer code;
    private final String name;

    public static boolean isEnabled(Integer code) {
        return ENABLED.code.equals(code);
    }

}
