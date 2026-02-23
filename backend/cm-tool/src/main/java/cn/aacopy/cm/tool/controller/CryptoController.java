package cn.aacopy.cm.tool.controller;

import cn.aacopy.cm.starter.web.pojo.Result;
import cn.aacopy.cm.tool.pojo.dto.CryptoProcessDTO;
import cn.aacopy.cm.tool.pojo.enums.CryptoModeEnum;
import cn.aacopy.cm.tool.pojo.enums.CryptoTypeEnum;
import cn.aacopy.cm.tool.pojo.vo.CryptoRsaKeyPairVO;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;

/**
 * @author cmyang
 * @date 2026/2/23
 */
@Tag(name = "加密解密")
@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @Operation(summary = "加密解密")
    @PostMapping("/process")
    public Result<String> process(@RequestBody @Validated CryptoProcessDTO cryptoProcessDTO) {
        String result = null;
        if (CryptoTypeEnum.MD5 == cryptoProcessDTO.getType()) {
            result = SecureUtil.md5(cryptoProcessDTO.getInput());
        } else if (CryptoTypeEnum.BASE64 == cryptoProcessDTO.getType()) {
            if (CryptoModeEnum.ENCRYPT == cryptoProcessDTO.getMode()) {
                result = Base64.encode(cryptoProcessDTO.getInput());
            } else {
                result = Base64.decodeStr(cryptoProcessDTO.getInput());
            }
        } else if (CryptoTypeEnum.URL == cryptoProcessDTO.getType()) {
            if (CryptoModeEnum.ENCRYPT == cryptoProcessDTO.getMode()) {
                result = URLUtil.encodeAll(cryptoProcessDTO.getInput());
            } else {
                result = URLUtil.decode(cryptoProcessDTO.getInput());
            }
        } else if (CryptoTypeEnum.AES == cryptoProcessDTO.getType()) {
            if (CryptoModeEnum.ENCRYPT == cryptoProcessDTO.getMode()) {
                result = SecureUtil.aes(cryptoProcessDTO.getKey().getBytes()).encryptHex(cryptoProcessDTO.getInput());
            } else {
                result = SecureUtil.aes(cryptoProcessDTO.getKey().getBytes()).decryptStr(cryptoProcessDTO.getInput());
            }
        } else if (CryptoTypeEnum.RSA == cryptoProcessDTO.getType()) {
            if (CryptoModeEnum.ENCRYPT == cryptoProcessDTO.getMode()) {
                result = SecureUtil.rsa(null, cryptoProcessDTO.getKey()).encryptHex(cryptoProcessDTO.getInput(), KeyType.PublicKey);
            } else {
                result = SecureUtil.rsa(cryptoProcessDTO.getKey(), null).decryptStr(cryptoProcessDTO.getInput(), KeyType.PrivateKey);
            }
        }
        return Result.success(result);
    }

    @Operation(summary = "生成RSA密钥对")
    @GetMapping("/rsaKeyPair")
    public Result<CryptoRsaKeyPairVO> rsaKeyPair(@RequestParam Integer keySize) {
        KeyPair keyPair = SecureUtil.generateKeyPair("RSA", keySize);
        RSA rsa = new RSA(keyPair.getPrivate(), keyPair.getPublic());
        CryptoRsaKeyPairVO vo = new CryptoRsaKeyPairVO();
        vo.setPublicKey(rsa.getPublicKeyBase64());
        vo.setPrivateKey(rsa.getPrivateKeyBase64());
        return Result.success(vo);
    }
}
