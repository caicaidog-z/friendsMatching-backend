
# 开发规范指南

为保证代码质量、可维护性、安全性与可扩展性，请在开发过程中严格遵循以下规范。

## 一、项目环境信息

- **操作系统**：Windows 11  
- **工作目录**：`D:\project\homie\homieMatching-release_2.0`
- **使用的 JDK 版本**：Java 8（虽然 pom.xml 中声明了 Java 1.8，但实际构建应使用 JDK 17）
- **使用的 SDK 版本**：
  - Spring Boot：2.7.16
  - MyBatis Plus：3.5.2
  - Redisson：3.17.5
  - MySQL Connector JDBC：8.x
- **构建工具**：Maven
- **作者**：小赵

---

## 二、目录结构说明

```
homieMatching-release_2.0/
├── doc/                 # 文档目录
├── logs/                # 日志文件存放目录
├── sql/                 # 数据库脚本目录
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── hjj/
    │   │           └── homieMatching/
    │   │               ├── aop/              # 切面相关逻辑
    │   │               ├── common/           # 公共组件或工具类
    │   │               ├── config/           # 配置类
    │   │               ├── constant/         # 常量定义
    │   │               ├── controller/       # 控制器层
    │   │               ├── exception/        # 自定义异常处理
    │   │               ├── job/              # 定时任务调度
    │   │               ├── manager/          # 管理类
    │   │               ├── mapper/           # 数据访问接口
    │   │               ├── model/
    │   │               │   ├── domain/       # 实体对象
    │   │               │   ├── dto/          # 数据传输对象
    │   │               │   ├── enums/        # 枚举类型
    │   │               │   ├── request/      # 请求参数封装
    │   │               │   └── vo/           # 视图展示对象
    │   │               ├── once/             # 一次性执行逻辑（如初始化等）
    │   │               ├── service/
    │   │               │   ├── blogInteractionStrategy/
    │   │               │   │   ├── enums/
    │   │               │   │   └── impl/
    │   │               │   ├── impl/         # Service 实现类
    │   │               │   └── ws/           # WebSocket 相关服务
    │   │               └── utils/            # 工具类
    │   └── resources/
    │       ├── config/                       # 配置文件目录
    │       └── mapper/                       # MyBatis Mapper XML 文件
    └── test/
        └── java/
            └── com/
                └── hjj/
                    └── homiematching/
                        └── service/
```

---

## 三、技术栈要求

- **主框架**：Spring Boot 2.7.16
- **语言版本**：Java 8（注意：尽管项目基于 Java 8 编译，建议统一升级至 JDK 17 以兼容新特性及安全更新）
- **核心依赖**：
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-redis`
  - `spring-session-data-redis`
  - `mybatis-plus-boot-starter`
  - `lombok`
  - `commons-beanutils`
  - `gson`
  - `easyexcel`
  - `hutool-all`
  - `aliyun-sdk-oss`
  - `cos_api`
  - `websocket`
  - `knife4j-spring-boot-starter`

---

## 四、分层架构规范

| 层级        | 职责说明                         | 开发约束与注意事项                                               |
|-------------|----------------------------------|----------------------------------------------------------------|
| **Controller** | 处理 HTTP 请求与响应，定义 API 接口 | 不得直接访问数据库，必须通过 Service 层调用                  |
| **Service**    | 实现业务逻辑、事务管理与数据校验   | 必须通过 Repository 或 Mapper 访问数据库；返回 DTO 而非 Entity（除非必要） |
| **Mapper**     | 数据库访问与持久化操作             | 使用 MyBatis Plus 进行 CRUD 操作                              |
| **Model**      | 包含各层级的数据模型（Domain, DTO, VO 等） | 分别用于不同层次间的数据传递                                 |

### 接口与实现分离

- 所有接口实现类需放在接口所在包下的 `impl` 子包中。
- 如：`UserService -> UserServiceImpl`

---

## 五、安全与性能规范

### 输入校验

- 使用 `@Valid` 和 JSR-303 校验注解（如 `@NotBlank`, `@Size` 等）
  - 注意：Spring Boot 2.x 中校验注解位于 `javax.validation.constraints.*`

- 禁止手动拼接 SQL 字符串，防止 SQL 注入攻击。

### 事务管理

- `@Transactional` 注解仅用于 **Service 层**方法。
- 避免在循环中频繁提交事务，影响性能。

---

## 六、代码风格规范

### 命名规范

| 类型       | 命名方式             | 示例                  |
|------------|----------------------|-----------------------|
| 类名       | UpperCamelCase       | `UserServiceImpl`     |
| 方法/变量  | lowerCamelCase       | `saveUser()`          |
| 常量       | UPPER_SNAKE_CASE     | `MAX_LOGIN_ATTEMPTS`  |

### 注释规范

- 所有类、方法、字段需添加 **Javadoc** 注释。
- 使用中文作为主要注释语言（符合用户第一语言要求）

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

---

## 七、扩展性与日志规范

### 接口优先原则

- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 包中（如 `UserServiceImpl`）。

### 日志记录

- 使用 `@Slf4j` 注解代替 `System.out.println`
- 日志级别控制合理，避免生产环境中输出过多 debug 内容

---

## 八、编码原则总结

| 原则       | 说明                                       |
|------------|--------------------------------------------|
| **SOLID**  | 高内聚、低耦合，增强可维护性与可扩展性     |
| **DRY**    | 避免重复代码，提高复用性                   |
| **KISS**   | 保持代码简洁易懂                           |
| **YAGNI**  | 不实现当前不需要的功能                     |
| **OWASP**  | 防范常见安全漏洞，如 SQL 注入、XSS 等      |

---
