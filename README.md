# 项目介绍

快捷API接口平台，**一个提供API接口供开发者快速调用的平台，用户可以注册登录，浏览接并调用已开通接口，并且每次调用会进行统计。**

管理员可以发布接口、下线接口、接入接口，以及可视化接口的调用情况、数据和管理接口的调用权限。

基一个于React+SpringBoot+MyBatis-Plus+Dubbo+Spring Cloud Gateway的API接口开放调用平台



# 业务流程

整个项目流程采用前台后台分离却紧密配合的方式

* 后台管理员可以对接口进行管理、分析
* 前台用户可以浏览接口、调用接口、查看个人信息，调用接口需要经过网关过滤才能调用到自己开发的SDK，真实的接口用户根本了解不到，一定程序上增强了接口调用的安全性

![](https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202307281435750.png)



# 技术选型

## 前端

- Ant Design Pro 5.x脚手架
- React 18
- Ant Design & Procomponents 组件库
- Umi 4 前端框架
- OpenAPI 前端代码生成
- Umi Request（Axios的封装）



## 后端

- Spring Boot
- MySQL
- MyBatis-Plus
- API签名认证（Http 调用）
- Spring Cloud Gateway 微服务网关
- Dobbo 分布式（RPC、Nacos）
- Swagger + Knife4j接口文档生成
- Spring Boot Starter（SDK开发）







# 页面展示

## 登录/注册页：

![image-20230811201115039](https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202308112011214.png)

## 主页（浏览接口）：

![image-20230811201432472](https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202308112014559.png)

## 个人中心页 ：

![image-20230811203045647](https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202308112030722.png)

## 在线调试接口：

![image-20230811202412732](https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202308112024811.png)

## 接口管理页（仅管理员）：

![image-20230811201513162](https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202308112015239.png)

## 接口调用次数统计（仅管理员）：

![image-20230811202950601](https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202308112029681.png)



# 项目总览 - 后端技术方案与架构

欢迎来到我们的项目-快捷API平台！下面向您介绍后端技术方案与架构，涵盖了项目的核心组件、功能模块以及我们所采用的技术工具。我们致力于为开发者和用户提供高效、安全、易用的接口服务。

## 项目背景与目标

我们的项目旨在构建一个全面的接口服务平台，包括模拟接口、后台管理、用户前台、API网关和第三方调用SDK等五个子系统。通过统一规范和高效管理，我们为开发者提供了一个灵活、可靠的接口调用平台，同时为用户提供了便捷的接口访问和管理方式。

## 项目架构概述

项目后端采用了一系列现代化技术，以满足高性能、可扩展性和安全性的要求。以下是我们项目的核心架构概述：

### 1. 多模块项目管理与依赖

我们将整个项目后端划分为五个子项目：web系统、模拟接口、公共模块、客户端SDK和API网关。使用Maven进行多模块依赖管理和打包，确保各个子项目之间的协作和集成高效有序。

### 2. 基础能力快速搭建

我们使用Ant Design Pro脚手架结合自建的Spring Boot项目模板，快速构建了初始的web项目。我们已经实现了前后端统一权限管理、多环境切换等基础能力，为项目的快速启动和开发奠定了基础。

### 3. 数据库操作与代码生成

基于MyBatis Plus框架的QueryWrapper，我们实现了对MySQL数据库的灵活查询。结合MyBatis X插件，自动生成后端CRUD基础代码，有效减少了重复的开发工作，提升了开发效率。

### 4. 接口文档与协作

我们使用Swagger和Knife4j自动生成符合OpenAPI规范的接口文档，为前后端协作提供了便利。前端可以在此基础上使用插件自动生成接口请求代码，降低了沟通成本，加速了开发进程。

### 5. 安全与认证

为保障接口调用的安全性和可溯源性，我们设计了API签名认证算法。每个用户都会被分配唯一的ak / sk用于鉴权，以确保接口调用的合法性和可追溯性。

### 6. 客户端SDK开发

为降低开发者调用成本，我们基于Spring Boot Starter开发了客户端SDK。使用者只需一行代码即可调用接口，大幅提升了开发体验，减少了繁琐的接口调用代码编写。

### 7. API网关与业务逻辑

我们选择了Spring Cloud Gateway作为API网关，实现了路由转发、访问控制、流量染色等功能。在网关层集中处理签名校验、请求参数校验、接口调用统计等业务逻辑，提高了系统的安全性和可维护性。

### 8. 子系统间高性能通信

为解决重复代码问题，我们将模型层和业务层代码抽象为公共模块，并使用Dubbo RPC框架实现了高性能的子系统间接口调用。这种设计有效减少了代码冗余，提高了系统的性能和可维护性。

## 五个子系统介绍

### 1. 模拟接口系统

提供丰富的模拟接口，供开发者使用和测试。例如，我们提供了随机头像生成接口等功能，帮助开发者在不同场景下快速获得测试数据。

### 2. 后台管理系统

管理员可以在此发布接口、设置调用数量、下线接口等。同时，可以查看用户对接口的使用情况，包括使用次数、错误调用等，为管理和监控提供了便捷途径。

### 3. 用户前台系统

为开发者提供了友好的访问界面，方便浏览所有接口。开发者可以购买或开通接口，并获取一定量的调用次数，使接口管理和使用更加便捷。

### 4. API网关系统

作为接口的流量控制中心，API网关负责计费统计、安全防护等功能，确保接口服务的一致性和质量。同时，简化了API的管理工作，提升了整体的开发效率。

### 5. 第三方调用SDK系统

为开发者提供了简化的工具包，使得调用接口更加方便。预封装了HTTP请求方法、接口调用示例等，帮助开发者轻松接入和使用我们的接口服务。



# 快速上手

在需要修改的地方只需要修改为自己的配置，比如MySQL和Redis相关配置，就可以正常运行了

## MySQL 数据库

1）修改 `application.yml` 的数据库配置为你自己的：

```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/my_db
    username: root
    password: xxx
```



2）执行 `sql/create_table.sql` 中的数据库语句，自动创建库表

3）启动项目，访问 `http://localhost:8101/api/doc.html` 即可打开接口文档，不需要写前端就能在线调试接口了~

[![img](https://github.com/l19556632521/springboot-module/raw/master/doc/swagger.png)](https://github.com/l19556632521/springboot-module/blob/master/doc/swagger.png)

## Redis 配置

1）修改 `application.yml` 的 Redis 配置为你自己的：

```
spring:
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
    password: xxx
```



2）修改 `application.yml` 中的 session 存储方式：

```
spring:
  session:
    store-type: redis
```



3）移除 `MainApplication` 类开头 `@SpringBootApplication` 注解内的 exclude 参数：

修改前：

```
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
```

修改后：

```
@SpringBootApplication
```



## 阿里云OOS对象存储

修改配置文件：

````yml

# 阿里云OSS配置
aliyun:
  oss:
    endpoint: oss-cn-nanjing.aliyuncs.com
    urlPrefix: http://nimble-api-project.oss-cn-hangzhou.aliyuncs.com
    accessKeyId: ${accessKeyId}
    accessKeySecret: ${accessKeySecret}
    bucketName: xxx
    fileHost: cache
````



## Dubbo注册中心

修改为你的注册中心配置

````yml
# Dubbo配置和Nacos
dubbo:
  application:
    name: xxx
  protocol:
    name: dubbo
    port: -1
  registry:
    address: nacos://localhost:8848
````





