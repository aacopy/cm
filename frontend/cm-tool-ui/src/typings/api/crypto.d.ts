declare namespace Api {
  /**
   * namespace Crypto
   *
   * backend api module: "crypto"
   */
  namespace Crypto {
    /** 加解密类型 */
    type CryptoType = 'MD5' | 'BASE64' | 'URL' | 'AES' | 'RSA';

    /** 操作模式 */
    type CryptoMode = 'ENCRYPT' | 'DECRYPT';

    /** 统一处理参数 */
    interface ProcessParams {
      /** 加解密类型 */
      type: CryptoType;
      /** 操作模式（md5 不需要） */
      mode?: CryptoMode;
      /** 输入内容 */
      input?: string;
      /** 密钥（AES/RSA 需要） */
      key?: string;
    }

    /** 统一处理结果 */
    type ProcessResult = string;

    /** RSA 密钥对 */
    interface RsaKeyPair {
      publicKey: string;
      privateKey: string;
    }
  }
}
