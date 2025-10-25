package cn.aacopy.cm.auth.common;

import lombok.Data;

/**
 * A generic result wrapper for API responses.
 * @author cmyang
 * @date 2025/10/24
 */
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(0);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> failure(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
