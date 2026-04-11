package cn.aacopy.cm.auth.login;

import cn.aacopy.cm.starter.web.exception.CommonErrorCode;
import cn.aacopy.cm.starter.web.exception.ServiceException;
import cn.aacopy.cm.starter.web.pojo.Result;
import cn.aacopy.cm.starter.web.util.I18nUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理认证过程中抛出的ServiceException异常的入口点
 * @author cmyang
 * @date 2026/2/28
 */
public class ExceptionEntryPoint {

    /**
     * 处理ServiceException
     * @param response
     * @param serviceException
     * @throws IOException
     */
    public static void commenceServiceException(HttpServletResponse response, ServiceException serviceException) throws IOException {
        serviceException.printStackTrace();
        response.setStatus(HttpServletResponse.SC_OK); // 还是返回200状态码
        response.setContentType("application/json;charset=UTF-8");
        String message = I18nUtil.translate(serviceException.getErrorCode(), serviceException.getArgs());
        int code = serviceException.getErrorCode().getCode();
        Result<Object> failureResult = Result.failure(code, message);
        response.getWriter().write(JSONUtil.toJsonStr(failureResult, JSONConfig.create().setIgnoreNullValue(false)));
    }

    /**
     * 处理其他异常
     * @param response
     * @param exception
     * @throws IOException
     */
    public static void commenceException(HttpServletResponse response, Exception exception) throws IOException {
        exception.printStackTrace();
        response.setStatus(HttpServletResponse.SC_OK); // 还是返回200状态码
        response.setContentType("application/json;charset=UTF-8");
        String message = I18nUtil.translate(CommonErrorCode.SERVER_ERROR);
        Result<Object> failureResult = Result.failure(CommonErrorCode.SERVER_ERROR.getCode(), message);
        response.getWriter().write(JSONUtil.toJsonStr(failureResult, JSONConfig.create().setIgnoreNullValue(false)));
    }
}
