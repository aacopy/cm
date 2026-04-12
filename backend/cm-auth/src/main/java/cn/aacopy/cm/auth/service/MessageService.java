package cn.aacopy.cm.auth.service;

import cn.aacopy.cm.auth.pojo.dto.SendVerificationCodeDTO;

/**
 * @author cmyang
 * @date 2026/4/11
 */
public interface MessageService {

    /**
     * 发送验证码
     * @param sendVerificationCodeDTO
     */
    void sendVerificationCode(SendVerificationCodeDTO sendVerificationCodeDTO);
}
