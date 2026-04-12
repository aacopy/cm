# cm-auth 认证中心

微服务项目的统一认证中心，提供 OAuth2 / OpenID Connect 授权、单点登录（SSO）及多种认证方式。

## 功能特性

- **OAuth2 授权服务器** — 授权码模式 + Refresh Token 自动轮换
- **OpenID Connect** — OIDC 扩展，支持用户信息端点
- **单点登录（SSO）** — 基于 Redis 会话的跨应用 SSO
- **邮箱验证码登录** — 免密登录，自动注册用户
- **多租户隔离** — 通过 auth_group 实现租户级数据隔离
- **JWK/RSA 签名** — JWT 令牌使用 RSA 密钥对签名

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 8 | 开发语言 |
| Spring Boot 2.7.18 | 应用框架 |
| Spring Security OAuth2 Authorization Server | OAuth2 / OIDC 实现 |
| Nacos | 服务注册与配置中心 |
| MyBatis Plus | ORM 框架 |
| Redis | 分布式会话 & 令牌缓存 |
| MySQL 5.7 | 持久化存储 |

## 模块结构

```
cm-auth
├── src/main/java/cn/aacopy/cm/auth/
│   ├── AuthApplication.java          # Spring Boot 入口
│   ├── common/                       # 公共组件（异常、常量、错误码）
│   ├── config/                       # 配置类（AuthProperties, AuthServerConfig）
│   ├── dao/                          # 数据访问层（AppDao, UserDao, AuthGroupDao）
│   ├── login/                        # 认证核心逻辑
│   │   ├── emailcode/                # 邮箱验证码认证
│   │   ├── sso/                      # SSO 相关逻辑
│   │   ├── RedisOAuth2AuthorizationService.java  # Redis 持久化的 OAuth2 服务
│   │   ├── SessionService.java       # 会话管理
│   │   └── DbRegisteredClientRepository.java     # DB 客户端仓库
│   ├── mapper/                       # MyBatis Plus Mapper
│   ├── pojo/
│   │   ├── entity/                   # 实体类（AppDO, UserDO, AuthGroupDO）
│   │   └── enums/                    # 枚举（LoginTypeEnum）
│   └── util/                         # 工具类
└── src/main/resources/
    └── application.yml               # 应用配置
```

## 配置说明

主要配置项（`application.yml` 或 Nacos 配置中心）：

```yaml
cm:
  auth:
    # 开发环境 Cookie 配置
    dev-cookie-domain: localhost
    dev-cookie-path: /
    # 生产环境 Cookie 配置
    cookie-domain: .aacopy.cn
    cookie-path: /
    # 登录页地址
    login-page: /login
```

## API 端点

### 认证相关

| 方法   | 路径                 | 说明      |
| ---- | ------------------ | ------- |
| POST | `/login/emailCode` | 邮箱验证码登录 |

请求体（`EmailCodeParam`）：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `authGroupId` | Long | 是 | 认证组 ID |
| `email` | String | 是 | 用户邮箱 |
| `code` | String | 是 | 验证码 |

### OAuth2 / OIDC

| 路径 | 说明 |
|------|------|
| `/oauth2/authorize` | 授权端点（含 SSO 拦截） |
| `/oauth2/token` | 令牌端点 |
| `/oauth2/revocation` | 令牌撤销 |
| `/.well-known/openid-configuration` | OIDC 发现配置 |
| `/userinfo` | OIDC 用户信息 |
| `/connect/logout` | OIDC 登出 |

### JWK

| 路径 | 说明 |
|------|------|
| `/oauth2/jwks.json` | JSON Web Key Set |

## 数据表

| 表名 | 说明 |
|------|------|
| `auth_group` | 认证组（租户隔离单元） |
| `app` | 注册的 OAuth2 客户端应用 |
| `user` | 终端用户 |

## Redis 键约定

| 键模式 | 说明 |
|--------|------|
| `AUTH:SESSION:*` | 用户会话信息 |
| `AUTH:TOKEN:ID:*` | OAuth2 授权记录（按 ID） |
| `AUTH:TOKEN:ST:*` | OAuth2 授权状态 |
| `AUTH:TOKEN:CD:*` | OAuth2 授权码 |
| `AUTH:EMAIL:CODE:*` | 邮箱验证码 |

## 认证流程

### 邮箱验证码登录

1. 客户端发送 `authGroupId` + `email` + `code` 到 `/login/emailCode`
2. 系统校验邮箱格式，验证验证码
3. 用户不存在时自动注册
4. 创建会话并返回令牌

### SSO 登录

1. 用户访问应用，重定向到 `/oauth2/authorize`
2. SSO Filter 检查 Redis 中是否存在有效会话
3. 已有会话 → 直接颁发授权码并跳转回调地址
4. 无会话 → 跳转登录页

## 错误码

认证相关错误码定义在 `ServiceErrorCode` 枚举中，通过 `ServiceAuthenticationException` 统一抛出。
