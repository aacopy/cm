package cn.aacopy.cm.tool.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cmyang
 * @date 2026/2/23
 */
@Schema(description = "RSA 密钥对")
@Data
public class CryptoRsaKeyPairVO {

    @Schema(description = "RSA 公钥")
    private String publicKey;

    @Schema(description = "RSA 私钥")
    private String privateKey;
}
