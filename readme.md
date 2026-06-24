# 机票预订系统

这是一个完整的机票预订系统，包含前端页面和 Spring Boot 后端服务。

## 📋 技术栈

| 分类 | 技术 | 版本 |
| :--- | :--- | :--- |
| 后端框架 | Spring Boot | 4.0.6 |
| 数据库 | MySQL | 8.0+ |
| Java版本 | OpenJDK | 25 |
| 构建工具 | Maven | 3.6+ |

## 📁 项目结构

机票预定系统v0.2/
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
| 用户注册 | POST | `/api/auth/register` | 新用户注册 |
| 用户登录 | POST | `/api/auth/login` | 用户登录 |
| 忘记密码 | POST | `/api/auth/forgot-password` | 密码找回 |

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
| 系统登录页 | 用户登录入口，支持用户名/密码登录 |
| 系统注册页 | 新用户注册，包含表单验证 |
| 系统忘记密码页 | 密码找回功能 |
| 系统首页 | 机票搜索主页面，包含热门航线、特价机票展示 |

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
    username: root                         # 数据库用户名
    password: your_password                # 数据库密码

  jpa:
    hibernate:
      ddl-auto: update                     # 自动更新表结构
    show-sql: true                        # 显示SQL语句（开发环境）
    database-platform: org.hibernate.dialect.MySQLDialect
```

### ⚠️ 注意事项

1. **数据库配置**：首次运行前必须修改 `application.yml` 中的数据库密码
2. **MySQL端口**：确保MySQL服务运行在默认端口 `3306`
3. **前端跨域**：后端已配置CORS，前端可直接访问
4. **环境要求**：确保安装了正确版本的JDK和Maven，以及MySQL数据库；
                确保有spring boot 4.0.6环境。

### 📄 许可证

Ciallo～(∠・ω< )⌒★ 那种东西当然是没有了

### 🤝 贡献

项目开发者：
- 项目设计：张子文
- 前端开发：邢凯鹏
- 后端开发：段一豪、邢凯鹏
- 数据库设计开发：杨开源
- 项目维护：商烁

| 联系邮箱：