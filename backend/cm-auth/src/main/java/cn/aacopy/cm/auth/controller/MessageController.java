package cn.aacopy.cm.auth.controller;

import cn.aacopy.cm.auth.pojo.dto.SendVerificationCodeDTO;
import cn.aacopy.cm.auth.service.MessageService;
import cn.aacopy.cm.starter.web.pojo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息
 * @author cmyang
 * @date 2026/4/11
 */
@Tag(name="消息")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @Operation(summary = "发送验证码")
    @PostMapping("/sendVerificationCode")
    public Result<Void> sendVerificationCode(@RequestBody @Validated SendVerificationCodeDTO sendVerificationCodeDTO) {
        messageService.sendVerificationCode(sendVerificationCodeDTO);
        return Result.success();
    }
}
