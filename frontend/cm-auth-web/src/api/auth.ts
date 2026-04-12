import request from './request'
import type {
  ApiResult,
  LoginParams,
  SendCodeParams,
  LoginResult,
} from '../types/auth'

export function sendEmailCode(params: SendCodeParams) {
  return request.post<ApiResult<null>>('/login/sendEmailCode', params)
}

export function loginByEmailCode(params: LoginParams) {
  return request.post<ApiResult<LoginResult>>('/login/emailCode', params)
}
