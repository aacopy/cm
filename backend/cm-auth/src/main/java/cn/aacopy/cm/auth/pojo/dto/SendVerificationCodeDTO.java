package cn.aacopy.cm.auth.pojo.dto;

import cn.aacopy.cm.auth.pojo.enums.VerificationCodeBizEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cmyang
 * @date 2026/4/11
 */
@Schema(description = "发送验证码入参")
@Data
public class SendVerificationCodeDTO {

    @Schema(description = "业务标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "biz 不能为空")
    private VerificationCodeBizEnum biz;

    @Schema(description = "接受验证码对象", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "receiver 不能为空")
    private String receiver;
}
