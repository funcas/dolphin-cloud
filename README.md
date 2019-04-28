## spring cloud dolphin

```
spring cloud 脚手架，具有统一授权、认证后台管理系统，其中包含具备用户管理、资源权限管理、网关API 管理等多个模块，支持多业务系统并行开发，
可以作为后端服务的开发脚手架。核心技术采用Spring Boot 2.1.3以及Spring Cloud (Greenwich.RELEASE) 相关核心组件，采用Nacos注册和配置
中心，集成流量卫兵Sentine（待整合，官方尚未完全支持Spring Cloud Gateway）

```

### 工程说明
```
+-- dolphin-auth                【认证中心模块】
|   +-- dolphin-auth-facade     【认证中心对外接口包】
|   +-- dolphin-auth-service    【认证中心核心服务 :9000】
+-- dolphin-common              【通用工具包】
+-- dolphin-concurrent          【并发相关工具包】
+-- dolphin-gateway             【网关服务 :7400】
+-- dolphin-oss                 【OSS工具包】
+-- dolphin-upms                【用户权限模块】
|   +-- dolphin-upms-facade     【用户权限对外接口包】
|   +-- dolphin-upms-service    【用户权限核心服务 :9001】
+-- dolphin-schedule            【elastic-job工具包】
```

#### todo list

* 扩展多种验证方式 (已完成)
* 服务降级  (已完成)
* 分布式事务集成 （Fescar）
* 分布式定时任务集成
* 微信开发工具集成
* 日志收集
* rabbitMQ
* oss客户端
* 支付接口
* 短信接口

