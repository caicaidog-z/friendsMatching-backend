
# 开发规范指南

为保证代码质量、可维护性、安全性与可扩展性，请在开发过程中严格遵循以下规范。

## 一、项目基础信息

- **工作目录**: `D:\project\chat\jujiao-yuan-backend-main\jujiao-yuan-backend-main`
- **操作系统**: Windows 11
- **使用框架**: JDK 17.0.4 + Maven
- **代码作者**: 小赵
- **当前时间**: 2025-09-23 21:53:11

### 目录结构

```
jujiao-yuan-backend-main
├── db
├── doc
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── qimu
    │   │           └── jujiao
    │   │               ├── common
    │   │               ├── config
    │   │               ├── contant
    │   │               ├── controller
    │   │               ├── exception
    │   │               ├── job
    │   │               ├── manager
    │   │               ├── mapper
    │   │               ├── model
    │   │               │   ├── entity
    │   │               │   ├── enums
    │   │               │   ├── file
    │   │               │   ├── request
    │   │               │   └── vo
    │   │               ├── service
    │   │               │   └── impl
    │   │               ├── utils
    │   │               └── ws
    │   └── resources
    │       └── mapper
    └── test
        └── java
            └── com
                └── qimu
                    └── jujiao
                        ├── manager
                        └── service
                            └── impl
```

## 二、技术栈要求

- **主框架**：Spring Boot 2.7.6
- **语言版本**：Java 1.8（注意：虽然构建环境是 JDK 17，但实际编译目标为 Java 1.8）
- **核心依赖**：
  - `spring-boot-starter-web`
  - `mybatis-plus-boot-starter`
  - `lombok`
  - `redis` (Spring Data Redis & Redisson)
  - `druid-spring-boot-starter`
  - `knife4j-spring-boot-starter`
  - `gson`, `hutool`, `commons-lang3`, `cos_api`

## 三、分层架构规范

| 层级        | 职责说明                         | 开发约束与注意事项                                               |
|-------------|----------------------------------|----------------------------------------------------------------|
| **Controller** | 处理 HTTP 请求与响应，定义 API 接口 | 不得直接访问数据库，必须通过 Service 层调用                  |
| **Service**    | 实现业务逻辑、事务管理与数据校验   | 必须通过 Mapper 或 Repository 访问数据库；返回 DTO 而非 Entity（除非必要） |
| **Mapper**     | 数据库访问与持久化操作             | 使用 MyBatis Plus 提供的方法进行查询；避免硬编码 SQL         |
| **Entity**     | 映射数据库表结构                   | 不得直接返回给前端（需转换为 DTO）；包名统一为 `model.entity` |

### 接口与实现分离

- 所有接口实现类需放在接口所在包下的 `impl` 子包中。
- 示例：`UserService` 对应 `UserServiceImpl`

## 四、安全与性能规范

### 输入校验

- 使用 `@Valid` 和 JSR-303 校验注解（如 `@NotBlank`, `@Size` 等）
  - 注意：由于使用的是 Spring Boot 2.x 版本，校验注解位于 `javax.validation.constraints.*`

- 禁止手动拼接 SQL 字符串，防止 SQL 注入攻击。

### 事务管理

- `@Transactional` 注解仅用于 **Service 层**方法。
- 避免在循环中频繁提交事务，影响性能。

## 五、代码风格规范

### 命名规范

| 类型       | 命名方式             | 示例                  |
|------------|----------------------|-----------------------|
| 类名       | UpperCamelCase       | `UserServiceImpl`     |
| 方法/变量  | lowerCamelCase       | `saveUser()`          |
| 常量       | UPPER_SNAKE_CASE     | `MAX_LOGIN_ATTEMPTS`  |

### 注释规范

- 所有类、方法、字段需添加 **Javadoc** 注释。
- 使用中文作为主要注释语言。

### 类型命名规范（阿里巴巴风格）

| 后缀 | 用途说明                     | 示例         |
|------|------------------------------|--------------|
| DTO  | 数据传输对象                 | `UserDTO`    |
| DO   | 数据库实体对象               | `UserDO`     |
| BO   | 业务逻辑封装对象             | `UserBO`     |
| VO   | 视图展示对象                 | `UserVO`     |
| Query| 查询参数封装对象             | `UserQuery`  |

### 实体类简化工具

- 使用 Lombok 注解替代手动编写 getter/setter/构造方法：
  - `@Data`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

## 六、扩展性与日志规范

### 接口优先原则

- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 包中（如 `UserServiceImpl`）。

### 日志记录

- 使用 `@Slf4j` 注解代替 `System.out.println`

## 七、其他通用规则

### 配置文件说明

- 主要配置文件：`application.yml`
- 支持多环境配置（dev, prod等）
- 数据源采用 Druid 连接池
- Redis 配置启用 Session Store 及缓存功能
- 使用腾讯云 COS 文件上传服务

### 架构设计建议

- 使用 MyBatis Plus 替代传统 JDBC 方式操作数据库
- 利用 Knife4j 提供 Swagger 文档支持
- WebSocket 模块用于实时通信场景
- 引入 Hutool 工具集提升开发效率
- 使用 Redisson 实现分布式锁等功能

## 八、编码原则总结

| 原则       | 说明                                       |
|------------|--------------------------------------------|
| **SOLID**  | 高内聚、低耦合，增强可维护性与可扩展性     |
| **DRY**    | 避免重复代码，提高复用性                   |
| **KISS**   | 保持代码简洁易懂                           |
| **YAGNI**  | 不实现当前不需要的功能                     |
| **OWASP**  | 防范常见安全漏洞，如 SQL 注入、XSS 等      |
