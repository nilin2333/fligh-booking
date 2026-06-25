# 机票预订系统

这是一个完整的机票预订系统，包含前端页面和 Spring Boot 后端服务。

## 📋 技术栈

### 后端技术
| 分类 | 技术 | 版本 |
| :--- | :--- | :--- |
| 后端框架 | Spring Boot | 4.0.6 |
| 数据库 | MySQL | 8.0+ |
| Java版本 | OpenJDK | 25 |
| 构建工具 | Maven | 3.6+ |
| ORM框架 | Hibernate | 6.4+ |
| 安全框架 | Spring Security | 6.2+ |

### 前端技术
| 分类 | 技术 | 版本 |
| :--- | :--- | :--- |
| 前端技术 | HTML5/CSS3/JavaScript ES6+ | 原生 |
| 样式框架 | 无 | - |
| 交互框架 | 无 | - |

## 📁 项目结构

机票预定系统v1.0.0/
 ├── 前端页面/                               # 前端HTML页面 
 │   ├── 系统登录页.html                     # 用户登录页面 
 │   ├── 系统注册页.html                     # 用户注册页面 
 │   ├── 系统忘记密码页.html                 # 忘记密码页面 
 │   └── 系统首页.html                      # 系统首页（机票搜索） 
 ├── 后端代码/                              # Spring Boot后端服务 
 │   ├── src/main/java/com/flightbooking/ 
 │   │ ├── controller/                      # REST API控制器 
 │   │ ├── service/                         # 业务逻辑层 
 │   │ ├── repository/                      # 数据访问层（JPA） 
 │   │ ├── entity/                          # 数据库实体 
 │   │ ├── dto/                             # 数据传输对象 
 │   │ ├── config/                          # 配置类（安全、Web） 
 │   │ ├── exception/                       # 全局异常处理 
 │   │ └── Application.java                 # Spring Boot启动类 
 │   ├── src/main/resources/ 
 │   │ └── application.yml                  # 应用配置 
 │   ├── target/                            # Maven构建输出 
 │   └── pom.xml                            # Maven依赖配置 
 ├── 数据库/                                # 数据库脚本 
 │   └── DataBase.sql                      # 数据库初始化脚本 
 └── README.md 

## 项目说明

### 🚀 快速开始

#### 环境要求

- ✅ JDK 25 或更高版本
- ✅ Maven 3.6 或更高版本
- ✅ MySQL 8.0 或更高版本
- ✅ Spring Boot 4.0.6 或更高版本

#### 安装步骤

1. **下载项目**
   ```bash
   # 直接下载压缩包并解压
   cd 机票预定系统v0.2
   ```

2. **配置数据库连接**
   
   编辑 `后端代码/src/main/resources/application.yml`，修改数据库密码：
   ```yaml
   spring:
     datasource:
       username: root
       password: your_password  # 替换为您的MySQL密码
   ```

3. **构建后端项目**
   ```bash
   cd 后端代码
   mvn clean package -DskipTests
   ```

4. **运行后端服务**
   ```bash
   # 方式一：使用Maven运行
   mvn spring-boot:run
   
   # 方式二：运行打包后的JAR文件
   java -jar target/flight-booking-system-1.0.0.jar
   ```

5. **访问前端页面**
   
   直接在浏览器中打开 `前端页面/系统首页.html` 即可使用。

### 🌐 访问地址

| 服务 | URL |
| :--- | :--- |
| 后端API | http://localhost:8080/flight-booking |
| 前端首页 | `前端页面/系统首页.html`（直接打开） |

### 🔌 API 接口

#### 用户认证接口

| 接口 | HTTP方法 | 路径 | 描述 |
| :--- | :--- | :--- | :--- |
| 用户注册 | POST | `/api/auth/register` | 新用户注册，包含邮箱验证 |
| 用户登录 | POST | `/api/auth/login` | 用户登录，返回JWT令牌 |
| 忘记密码 | POST | `/api/auth/forgot-password` | 密码找回，发送重置邮件 |
| 重置密码 | POST | `/api/auth/reset-password` | 重置密码，需要验证令牌 |

#### 机票查询接口

| 接口 | HTTP方法 | 路径 | 描述 |
| :--- | :--- | :--- | :--- |
| 搜索机票 | GET | `/api/flights/search` | 根据出发地、目的地、日期搜索机票 |
| 获取热门航线 | GET | `/api/flights/hot` | 获取热门航线列表 |
| 获取特价机票 | GET | `/api/flights/discount` | 获取特价机票列表 |

#### 订单管理接口

| 接口 | HTTP方法 | 路径 | 描述 |
| :--- | :--- | :--- | :--- |
| 创建订单 | POST | `/api/orders` | 创建新订单 |
| 获取我的订单 | GET | `/api/orders/my` | 获取当前用户的订单列表 |
| 取消订单 | PUT | `/api/orders/{id}/cancel` | 取消未支付订单 |

#### 注册接口示例

**请求:**
```json
POST /api/auth/register
Content-Type: application/json

{
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "password": "123456",
    "phone": "13800138000"
}
```

**响应:**
```json
{
    "success": true,
    "message": "注册成功",
    "data": {
        "id": 1,
        "username": "zhangsan",
        "email": "zhangsan@example.com",
        "phone": "13800138000"
    }
}
```

#### 登录接口示例

**请求:**
```json
POST /api/auth/login
Content-Type: application/json

{
    "username": "zhangsan",
    "password": "123456"
}
```

**响应:**
```json
{
    "success": true,
    "message": "登录成功",
    "data": {
        "id": 1,
        "username": "zhangsan",
        "email": "zhangsan@example.com"
    }
}
```

### 📝 前端页面功能

| 页面 | 功能描述 |
| :--- | :--- |
| 系统登录页 | 用户登录入口，支持用户名/密码登录，包含表单验证 |
| 系统注册页 | 新用户注册，包含邮箱、手机号格式验证和密码强度检查 |
| 系统忘记密码页 | 密码找回功能，支持邮箱验证码验证 |
| 系统首页 | 机票搜索主页面，包含热门航线、特价机票展示，支持多条件筛选 |

### ⚙️ 配置说明

#### application.yml 关键配置

```yaml
server:
  port: 8080                              # 服务端口
  servlet:
    context-path: /flight-booking         # 上下文路径

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/flight_booking_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&characterEncoding=utf8
    username: your_name                    # 数据库用户名
    password: your_password                # 数据库密码

  jpa:
    hibernate:
      ddl-auto: update                     # 自动更新表结构
    show-sql: true                        # 显示SQL语句（开发环境）
    database-platform: org.hibernate.dialect.MySQLDialect
```

### ⚠️ 注意事项

1. **数据库配置**：首次运行前必须修改 `application.yml` 中的数据库密码和用户名
2. **MySQL端口**：确保MySQL服务运行在默认端口 `3306`，若修改端口需同步更新`application.yml`
3. **前端跨域**：后端已配置CORS，前端可直接访问后端API，无需额外配置
4. **环境要求**：确保安装了正确版本的JDK和Maven，以及MySQL数据库
5. **依赖安装**：首次运行后端项目前需执行`mvn clean install`安装依赖
6. **数据库初始化**：首次运行会自动创建数据库表结构，建议先创建空数据库`flight_booking_db`

### 📄 许可证

MIT License

### 📞 联系方式

| 类型 | 信息 |
| :--- | :--- |
| 联系邮箱 | flightbooking@example.com |
| 项目地址 | [https://github.com/yourusername/flight-booking-system](https://github.com/yourusername/flight-booking-system) |