# cm-auth 接口文档

认证中心 API 接口定义，供前端开发参考。

## 通用说明

### 请求格式

- 所有 POST 接口请求体为 `application/json` 格式
- 参数命名使用 camelCase

### 错误响应

认证相关错误码定义在 `ServiceErrorCode` 枚举中，通过 `ServiceAuthenticationException` 统一抛出。

---

## 认证接口

### 1. 发送邮箱验证码

> 向用户邮箱发送6位数字验证码，验证码有效期5分钟。

- **URL**: `POST /login/sendEmailCode`
- **Content-Type**: `application/json`

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `authGroupId` | Long | 是 | 认证组 ID（租户标识） |
| `email` | String | 是 | 用户邮箱地址 |

**请求示例：**

```json
{
  "authGroupId": 1,
  "email": "user@example.com"
}
```

**响应示例（成功）：**

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**响应说明：**

- 返回成功仅表示验证码已发送，不返回验证码内容
- 同一邮箱发送频率限制：60秒内只能发送一次
- 验证码有效期：5分钟

---

### 2. 邮箱验证码登录

> 使用邮箱+验证码登录，验证通过自动注册用户（如不存在）。

- **URL**: `POST /login/emailCode`
- **Content-Type**: `application/json`

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `authGroupId` | Long | 是 | 认证组 ID |
| `email` | String | 是 | 用户邮箱 |
| `code` | String | 是 | 6位数字验证码 |

**请求示例：**

```json
{
  "authGroupId": 1,
  "email": "user@example.com",
  "code": "123456"
}
```

**响应示例（成功）：**

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "userId": 1001,
    "email": "user@example.com",
    "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**常见错误：**

| 错误 | 说明 |
|------|------|
| 验证码错误 | 验证码不匹配或已过期 |
| 邮箱格式错误 | 邮箱地址格式不合法 |
| 发送频率过高 | 60秒内重复发送 |

---

## OAuth2 / OIDC 端点

### 授权端点

| 路径 | 方法 | 说明 |
|------|------|------|
| `/oauth2/authorize` | GET/POST | OAuth2 授权端点（含 SSO 拦截） |
| `/oauth2/token` | POST | 令牌端点 |
| `/oauth2/revocation` | POST | 令牌撤销 |

### OIDC 发现

| 路径 | 说明 |
|------|------|
| `/.well-known/openid-configuration` | OIDC 发现配置文档 |
| `/userinfo` | OIDC 用户信息端点 |
| `/connect/logout` | OIDC 登出端点 |

### JWK

| 路径                  | 说明                           |
| ------------------- | ---------------------------- |
| `/oauth2/jwks.json` | JSON Web Key Set，用于验证 JWT 签名 |

---

## 数据模型

### 用户表 `user`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 用户 ID |
| `email` | String | 邮箱地址 |
| `auth_group_id` | Long | 所属认证组 ID |

### 认证组表 `auth_group`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 认证组 ID |
| `name` | String | 认证组名称 |

### 应用表 `app`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 应用 ID |
| `client_id` | String | OAuth2 客户端 ID |
| `client_secret` | String | OAuth2 客户端密钥 |
| `redirect_uris` | String | 回调地址 |
