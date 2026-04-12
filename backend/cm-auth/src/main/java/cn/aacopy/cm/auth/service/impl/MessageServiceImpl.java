package cn.aacopy.cm.auth.service.impl;

import cn.aacopy.cm.auth.pojo.dto.SendVerificationCodeDTO;
import cn.aacopy.cm.auth.service.MessageService;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

/**
 * @author cmyang
 * @date 2026/4/11
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public void sendVerificationCode(SendVerificationCodeDTO sendVerificationCodeDTO) {
        System.out.println("发送验证码 " + JSONUtil.toJsonStr(sendVerificationCodeDTO));
        //TODO
    }
}
