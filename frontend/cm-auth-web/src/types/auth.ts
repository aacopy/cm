export interface ApiResult<T> {
  code: number
  message: string
  data: T | null
}

export interface LoginParams {
  authGroupId: number
  email: string
  code: string
}

export interface SendCodeParams {
  authGroupId: number
  email: string
}

export interface LoginResult {
  userId: number
  email: string
  token: string
}
