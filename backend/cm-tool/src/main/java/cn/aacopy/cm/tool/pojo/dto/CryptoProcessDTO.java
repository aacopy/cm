package cn.aacopy.cm.tool.pojo.dto;

import cn.aacopy.cm.tool.pojo.enums.CryptoModeEnum;
import cn.aacopy.cm.tool.pojo.enums.CryptoTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cmyang
 * @date 2026/2/23
 */
@Schema(description = "加解密参数")
@Data
public class CryptoProcessDTO {

    @Schema(description = "加解密类型")
    @NotNull
    private CryptoTypeEnum type;

    @Schema(description = "加解密模式")
    private CryptoModeEnum mode;

    @Schema(description = "输入内容")
    @NotBlank
    private String input;

    @Schema(description = "加解密密钥")
    private String key;
}
