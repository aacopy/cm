import { request } from '../request';

/** 生成雪花 ID（由后端生成） */
export function fetchGenerateSnowflakeId() {
  return request<Api.Generate.SnowflakeId>({
    url: '/tool/generate/snowflake'
  });
}

/**
 * 生成 UUID（由后端生成）
 *
 * @param withHyphen 是否带 '-'
 */
export function fetchGenerateUuid(withHyphen: boolean) {
  return request<Api.Generate.UUID>({
    url: '/tool/generate/uuid',
    params: { withHyphen: withHyphen ? 'Y' : 'N' }
  });
}

/**
 * 生成随机字符串（由后端生成，支持指定长度）
 *
 * @param length 长度
 */
export function fetchGenerateRandomString(length: number) {
  return request<Api.Generate.RandomString>({
    url: '/tool/generate/randomString',
    params: { length }
  });
}
