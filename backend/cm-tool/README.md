# CM-Tool 工具服务
## 📋 功能特性

### 🔐 加密解密服务
- **MD5** - 单向散列加密
- **BASE64** - 编码/解码
- **URL 编码** - URL 字符串编码/解码
- **AES** - 对称加密（需提供密钥）
- **RSA** - 非对称加密（支持密钥对生成）

### 🎲 数据生成服务
- **雪花算法 ID** - 分布式唯一ID生成
- **UUID** - 支持带/不带连字符的UUID生成
- **随机字符串** - 指定长度的随机字符串生成

---

## 🚀 快速开始

### 前置条件

- JDK 8 或更高版本
- Maven 3.6 或更高版本
- Nacos 配置中心（可选，默认本地）

### 编译构建

```bash
# 克隆项目
git clone <repository-url>
cd cm-tool

# 编译打包
mvn clean package

# 或使用 Maven Wrapper
mvn clean package
```

### 运行应用

```bash
# 直接运行 JAR
java -jar target/tool.jar

# 或使用 Maven 运行
mvn spring-boot:run
```

应用将在 `http://localhost:8003` 启动

---

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 11+ | 编程语言 |
| Spring Boot | 3.0+ | Web 框架 |
| Maven | 3.6+ | 项目构建 |
| Hutool | - | 加密/解密工具库 |
| Nacos | - | 分布式配置中心 |
| Swagger/OpenAPI | 3.0 | API 文档 |
| Lombok | - | 代码生成库 |

---

## 🔐 安全建议

1. **生产环境配置**
   - 修改 Nacos 默认密码
   - 配置 HTTPS 支持
   - 添加请求认证和授权

2. **加密使用建议**
   - AES 密钥长度建议 16/24/32 字符
   - RSA 密钥长度建议 2048 位或更高
   - 敏感数据建议使用 RSA 非对称加密

3. **数据安全**
   - 避免在日志中输出敏感数据
   - 定期更新依赖包以修复安全漏洞
   - 实施请求限流防止滥用

---

## 📝 API 快速参考

| 功能 | 方法 | 路径 | 参数 |
|------|------|------|------|
| 加密/解密 | POST | `/crypto/process` | type, mode, input, key |
| 生成RSA密钥对 | GET | `/crypto/rsaKeyPair` | keySize |
| 生成雪花ID | GET | `/generate/snowflake` | 无 |
| 生成UUID | GET | `/generate/uuid` | withHyphen |
| 生成随机字符串 | GET | `/generate/randomString` | length |

