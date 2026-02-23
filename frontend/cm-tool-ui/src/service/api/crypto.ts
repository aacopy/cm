import { request } from '../request';

/**
 * 统一的加解密处理接口
 *
 * @param params 加解密参数
 */
export function fetchCryptoProcess(params: Api.Crypto.ProcessParams) {
  return request<Api.Crypto.ProcessResult>({
    url: '/tool/crypto/process',
    method: 'post',
    data: params
  });
}

/**
 * 生成 RSA 密钥对
 *
 * @param keySize 密钥长度（1024、2048、4096）
 */
export function fetchCryptoRsaGenerateKeyPair(keySize: number) {
  return request<Api.Crypto.RsaKeyPair>({
    url: '/tool/crypto/rsaKeyPair',
    params: { keySize }
  });
}
