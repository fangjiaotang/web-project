# 活动报名系统 - 运行指南

## 系统架构
本系统是一个基于Spring Boot和Spring Cloud的微服务架构，包含以下组件：
- API网关 (api-gateway)
- 用户服务 (user-service)
- 活动服务 (activity-service)
- 报名服务 (registration-service)
- 服务注册中心 (Eureka Server，需单独部署)
- 中间件：MySQL、Redis、Kafka

## 环境准备

### 1. 安装必要的软件

#### Java 开发环境
- **JDK 17+**：确保正确设置`JAVA_HOME`环境变量
  - 下载地址：https://www.oracle.com/java/technologies/downloads/
  - 验证安装：`java -version`和`javac -version`

#### Maven 构建工具
- **Maven 3.6+**：用于构建项目
  - 下载地址：https://maven.apache.org/download.cgi
  - 验证安装：`mvn -version`

#### Docker 容器化环境
- **Docker Desktop**：推荐用于Windows/macOS
  - 下载地址：
    - Windows：https://www.docker.com/products/docker-desktop/
    - macOS：https://www.docker.com/products/docker-desktop/
  - 安装完成后，启动Docker Desktop并确保Docker服务正在运行
  - 验证安装：`docker --version`和`docker compose version`

- **Docker Engine + Docker Compose**：用于Linux
  - 安装Docker Engine：https://docs.docker.com/engine/install/
  - 安装Docker Compose：https://docs.docker.com/compose/install/
  - 验证安装：`docker --version`和`docker compose version`

### 2. 克隆或下载项目
```bash
git clone <repository-url>
cd web project
```

## 使用Docker Compose快速启动中间件

本项目提供了完整的Docker Compose配置，可以快速启动所有必要的中间件服务：
- Eureka Server（服务注册中心）
- MySQL（数据库）
- Redis（缓存和限流）
- Kafka + Zookeeper（消息队列）
- Prometheus + Grafana（监控）
- ELK Stack（日志收集和分析）

### 1. 启动Docker Compose服务
```bash
# 启动所有服务（后台运行）
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看服务日志（例如查看MySQL日志）
docker-compose logs -f mysql
```

### 2. 验证中间件服务
- **Eureka控制台**：http://localhost:8761
- **MySQL**：端口3306（用户名：root，密码：root_password）
- **Redis**：端口6379
- **Kafka Manager**：http://localhost:9000
- **Prometheus**：http://localhost:9090
- **Grafana**：http://localhost:3000（默认用户名/密码：admin/admin）
- **Kibana**：http://localhost:5601

### 3. 停止服务
```bash
# 停止所有服务
docker-compose down

# 停止并删除数据卷
docker-compose down -v
```

## 数据库配置

### 使用Docker Compose（推荐）
如果您使用Docker Compose启动中间件，MySQL数据库和表结构会通过初始化脚本自动创建，包括：
- 所有必要的数据库：user_db, user_db_1, user_db_2, activity_db, activity_db_1, activity_db_2, registration_db
- 完整的表结构和测试数据

**默认连接信息：**
- 主机：localhost
- 端口：3306
- 用户名：root
- 密码：root_password

### 手动配置（可选）
如果您不使用Docker Compose，可以手动配置所有中间件服务。以下是详细步骤：

#### 1. MySQL数据库安装与配置

##### 1.1 安装MySQL
- **Windows**：下载MySQL Installer from https://dev.mysql.com/downloads/installer/
- **macOS**：使用Homebrew: `brew install mysql`
- **Linux**：使用包管理器，如Ubuntu: `sudo apt-get install mysql-server`

##### 1.2 配置MySQL
1. 启动MySQL服务：
   - Windows: `net start mysql`
   - macOS/Linux: `sudo systemctl start mysql`

2. 设置root密码：
   ```bash
   # 登录MySQL
   mysql -u root
   
   # 设置密码
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'root_password';
   FLUSH PRIVILEGES;
   ```

3. 创建必要的数据库：
   ```sql
   -- 用户服务数据库
   CREATE DATABASE IF NOT EXISTS `user_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE DATABASE IF NOT EXISTS `user_db_1` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE DATABASE IF NOT EXISTS `user_db_2` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 活动服务数据库
   CREATE DATABASE IF NOT EXISTS `activity_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE DATABASE IF NOT EXISTS `activity_db_1` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE DATABASE IF NOT EXISTS `activity_db_2` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 报名服务数据库
   CREATE DATABASE IF NOT EXISTS `registration_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. 导入表结构和测试数据：
   ```bash
   mysql -u root -p < docker/mysql/init.sql
   ```

#### 2. Redis安装与配置

##### 2.1 安装Redis
- **Windows**：下载Redis for Windows from https://github.com/tporadowski/redis/releases
- **macOS**：使用Homebrew: `brew install redis`
- **Linux**：使用包管理器，如Ubuntu: `sudo apt-get install redis-server`

##### 2.2 配置Redis
1. 启动Redis服务：
   - Windows: 双击redis-server.exe
   - macOS/Linux: `redis-server`

2. 验证Redis运行状态：
   ```bash
   redis-cli ping
   ```
   如果返回`PONG`，表示Redis已成功启动。

#### 3. Kafka安装与配置

##### 3.1 安装Kafka
1. 下载Kafka from https://kafka.apache.org/downloads
2. 解压到指定目录：
   ```bash
   tar -xzf kafka_2.13-3.3.1.tgz
   cd kafka_2.13-3.3.1
   ```

##### 3.2 启动Kafka服务
1. 启动Zookeeper（Kafka依赖）：
   ```bash
   bin/zookeeper-server-start.sh config/zookeeper.properties
   ```

2. 启动Kafka服务器：
   ```bash
   bin/kafka-server-start.sh config/server.properties
   ```

3. 创建必要的主题：
   ```bash
   bin/kafka-topics.sh --create --topic registration-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
   ```

#### 4. Eureka Server安装与配置

##### 4.1 创建Eureka Server项目
1. 创建一个新的Spring Boot项目，添加`Spring Cloud Starter Netflix Eureka Server`依赖
2. 主类添加`@EnableEurekaServer`注解

##### 4.2 配置Eureka Server
在`application.properties`中添加：
```properties
server.port=8761
spring.application.name=eureka-server
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

##### 4.3 启动Eureka Server
```bash
java -jar eureka-server-1.0.0.jar
```

#### 5. 配置服务连接
修改各个服务的配置文件，设置正确的中间件连接信息：

- **用户服务**：`microservices/user-service/src/main/resources/application.properties`
- **活动服务**：`microservices/activity-service/src/main/resources/application.properties`
- **报名服务**：`microservices/registration-service/src/main/resources/application.properties`

确保所有服务的配置文件中：
- 数据库连接使用正确的用户名、密码和数据库名
- Redis配置指向正确的主机和端口
- Kafka配置指向正确的bootstrap服务器
- Eureka配置指向正确的注册中心地址

## 中间件配置

### 使用Docker Compose时的配置（推荐）
当您使用Docker Compose时，Redis、Kafka和Eureka Server的配置已经自动完成：
- Redis：端口6379，无密码
- Kafka：端口9092，已创建`registration-topic`主题
- Eureka Server：端口8761，服务地址`http://localhost:8761/eureka/`

### 手动配置（可选）

#### 1. Redis配置
确保Redis服务正常运行，默认端口6379。如需修改配置，更新各个服务的application.properties：
```properties
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
```

#### 2. Kafka配置
启动Kafka服务，并创建必要的主题：
```bash
# 创建主题
bin/kafka-topics.sh --create --topic registration-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

如需修改Kafka配置，更新报名服务的application.properties：
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=registration-group
```

#### 3. Eureka Server配置
创建并启动Eureka Server，默认端口8761。更新各个服务的application.properties，设置Eureka地址：
```properties
spring.application.name=service-name
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## 构建项目

使用Maven构建所有服务：

```bash
# 构建API网关
cd api-gateway
mvn clean install

# 构建用户服务
cd ../microservices/user-service
mvn clean install

# 构建活动服务
cd ../activity-service
mvn clean install

# 构建报名服务
cd ../registration-service
mvn clean install
```

## 启动服务

### 启动顺序（重要）
1. **Eureka Server**：服务注册中心
2. **MySQL、Redis、Kafka**：中间件服务
3. **用户服务**：基础服务
4. **活动服务**：依赖用户服务
5. **报名服务**：依赖用户服务和活动服务
6. **API网关**：最后启动，路由所有请求

### 启动命令

```bash
# 启动Eureka Server（单独部署）
java -jar eureka-server-1.0.0.jar

# 启动用户服务
cd microservices/user-service
java -jar target/user-service-1.0.0.jar

# 启动活动服务
cd ../activity-service
java -jar target/activity-service-1.0.0.jar

# 启动报名服务
cd ../registration-service
java -jar target/registration-service-1.0.0.jar

# 启动API网关
cd ../../api-gateway
java -jar target/api-gateway-1.0.0.jar
```

## 验证服务

### 1. 检查服务注册
访问Eureka控制台：http://localhost:8761
确保所有服务都已成功注册。

### 2. 测试API接口

#### 获取可用活动
```bash
curl -X GET http://localhost:8080/api/activities/available
```

#### 用户报名活动
```bash
# 首先获取验证码
curl -X GET http://localhost:8080/api/captcha
# 返回：{"captchaId": "xxx", "captchaImage": "base64-image"}

# 提交报名请求
curl -X POST http://localhost:8080/api/registrations \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "activityId": 1,
    "captchaId": "xxx",
    "captchaCode": "123456"
  }'
```

## 完整运行流程

### 方式一：使用Docker Compose（推荐）

#### 步骤1：安装并启动Docker
1. 按照环境准备部分的说明安装Docker Desktop
2. 启动Docker Desktop并确保Docker服务正在运行
3. 验证Docker安装：
   ```bash
   docker --version
   docker compose version
   ```

#### 步骤2：启动中间件服务
1. 进入项目根目录
2. 启动所有中间件服务：
   ```bash
   docker compose up -d
   ```
3. 等待约1-2分钟，确保所有服务都正常启动
4. 检查服务状态：
   ```bash
   docker compose ps
   ```
   所有服务状态应为"Up"

#### 步骤3：构建项目
1. 在项目根目录执行Maven构建命令：
   ```bash
   # 构建所有微服务和API网关
   mvn clean install
   ```

#### 步骤4：启动微服务（按顺序）

1. **用户服务**：
   ```bash
   java -jar microservices/user-service/target/user-service-1.0.0.jar
   ```

2. **活动服务**（在新终端窗口中）：
   ```bash
   java -jar microservices/activity-service/target/activity-service-1.0.0.jar
   ```

3. **报名服务**（在新终端窗口中）：
   ```bash
   java -jar microservices/registration-service/target/registration-service-1.0.0.jar
   ```

4. **API网关**（在新终端窗口中）：
   ```bash
   java -jar api-gateway/target/api-gateway-1.0.0.jar
   ```

> **提示**：如果需要在后台运行服务，可以在命令末尾添加`&`符号。

#### 步骤5：验证服务

1. **检查服务注册**：
   - 访问Eureka控制台：http://localhost:8761
   - 确保所有服务都已成功注册（显示为"UP"状态）

2. **测试API接口**：
   - 获取可用活动：
     ```bash
     curl -X GET http://localhost:8080/api/activities/available
     ```

   - 获取验证码：
     ```bash
     curl -X GET http://localhost:8080/api/captcha
     ```
     预期返回：`{"captchaId": "xxx", "captchaImage": "base64-encoded-image"}`

   - 用户报名活动（使用获取到的验证码）：
     ```bash
     curl -X POST http://localhost:8080/api/registrations \
       -H "Content-Type: application/json" \
       -d '{
         "userId": 1,
         "activityId": 1,
         "captchaId": "获取到的captchaId",
         "captchaCode": "输入验证码"
       }'
     ```
     预期返回：报名成功信息

### 方式二：手动安装中间件

#### 步骤1：安装并配置中间件
按照"环境准备"部分的说明，手动安装并配置：
- JDK 17+
- Maven 3.6+
- MySQL 8+
- Redis 6+
- Kafka 3+
- Eureka Server

#### 步骤2：配置中间件连接
修改各个服务的配置文件，设置正确的中间件连接信息：
- **用户服务**：`microservices/user-service/src/main/resources/application.properties`
- **活动服务**：`microservices/activity-service/src/main/resources/application.properties`
- **报名服务**：`microservices/registration-service/src/main/resources/application.properties`

#### 步骤3：启动中间件服务
1. 启动MySQL服务
2. 启动Redis服务
3. 启动Zookeeper和Kafka服务
4. 启动Eureka Server

#### 步骤4：构建项目
```bash
mvn clean install
```

#### 步骤5：启动微服务（按顺序）
同方式一的步骤4

#### 步骤6：验证服务
同方式一的步骤5

### 完整运行演示

以下是一个完整的运行演示流程，从环境准备到API测试：

```bash
# 1. 检查环境
java -version
mvn -version
docker --version

# 2. 启动中间件
docker compose up -d

# 3. 构建项目
mvn clean install

# 4. 启动微服务（分别在不同终端）
# 终端1：用户服务
java -jar microservices/user-service/target/user-service-1.0.0.jar

# 终端2：活动服务
java -jar microservices/activity-service/target/activity-service-1.0.0.jar

# 终端3：报名服务
java -jar microservices/registration-service/target/registration-service-1.0.0.jar

# 终端4：API网关
java -jar api-gateway/target/api-gateway-1.0.0.jar

# 5. 验证服务注册
curl -X GET http://localhost:8761/eureka/apps/

# 6. 测试API
# 获取可用活动
curl -X GET http://localhost:8080/api/activities/available

# 获取验证码
CAPTCHA_RESPONSE=$(curl -X GET http://localhost:8080/api/captcha)
CAPTCHA_ID=$(echo $CAPTCHA_RESPONSE | grep -o '"captchaId":"[^"]*"' | cut -d '"' -f 4)

# 报名活动（请手动输入验证码）
curl -X POST http://localhost:8080/api/registrations \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "activityId": 1, "captchaId": "'$CAPTCHA_ID'", "captchaCode": "输入验证码"}'
```

## 运行系统后可访问的地址

| 服务/工具 | 地址 | 用途 |
|---------|------|------|
| Eureka控制台 | http://localhost:8761 | 服务注册与发现 |
| API网关 | http://localhost:8080 | 所有API请求入口 |
| MySQL | localhost:3306 | 数据库 |
| Redis | localhost:6379 | 缓存和限流 |
| Kafka Manager | http://localhost:9000 | Kafka管理界面 |
| Prometheus | http://localhost:9090 | 监控数据收集 |
| Grafana | http://localhost:3000 | 监控数据可视化 |
| Kibana | http://localhost:5601 | 日志管理和分析 |

## 常见问题与解决方案

### 1. 服务启动失败
- 检查端口是否被占用：使用`netstat -ano | findstr <端口号>`（Windows）或`lsof -i :<端口号>`（Linux/macOS）
- 检查数据库连接配置：确保用户名、密码和数据库名与docker-compose配置一致
- 检查中间件服务是否正常运行：使用`docker-compose ps`查看服务状态
- 查看日志文件定位错误：服务日志位于`logs/`目录或控制台输出

### 2. 服务注册失败
- 检查Eureka Server地址是否正确：确认配置文件中的`eureka.client.service-url.defaultZone`是否为`http://localhost:8761/eureka/`
- 检查网络连接：确保服务可以访问Eureka Server
- 检查服务名称是否唯一：每个微服务的`spring.application.name`必须唯一

### 3. API请求失败
- 检查API网关是否正常运行：确认网关服务已启动且端口8080可用
- 检查请求路径是否正确：参考API文档或测试示例
- 检查微服务是否正常注册：在Eureka控制台确认服务状态为UP
- 查看API网关和微服务的日志：定位具体错误信息

## 监控与管理

### 1. 监控页面
- **Eureka控制台**：http://localhost:8761
- **Actuator端点**：http://localhost:service-port/actuator
- **Prometheus**：http://localhost:9090
- **Grafana**：http://localhost:3000

### 2. 日志管理
- 日志文件位于各个服务的logs目录
- 支持ELK Stack集中管理日志

## 开发环境配置

### IDE推荐
- IntelliJ IDEA
- Eclipse with Spring Tools Suite

### 调试配置
1. 在IDE中导入Maven项目
2. 设置JDK版本为17
3. 配置各个服务的运行参数
4. 启动调试模式

## 部署建议

### 生产环境
- 使用Docker容器化部署
- 使用Kubernetes进行编排
- 配置负载均衡和高可用
- 实施监控和告警系统

### 测试环境
- 可以使用Docker Compose快速部署
- 配置简化版的中间件服务

## 技术栈

- **框架**：Spring Boot 3.1.0, Spring Cloud 2022.0.3
- **构建工具**：Maven 3.6+
- **数据库**：MySQL 8+, ShardingSphere-JDBC
- **缓存**：Redis 6+
- **消息队列**：Kafka 2.8+
- **服务注册与发现**：Eureka
- **API网关**：Spring Cloud Gateway
- **监控**：Prometheus, Grafana, Spring Boot Actuator
- **日志**：Logback, ELK Stack

## 联系方式

如有问题，请联系系统管理员。
