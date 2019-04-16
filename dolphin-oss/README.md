### oss模块

```
目前整合了阿里oss和七牛等两个平台
```

#### 使用方式

* 添加maven依赖，并添加对应平台的Sdk依赖，oss模块默认不传递
```
<dependency>
    <groupId>cn.goktech</groupId>
    <artifactId>dolphin-oss</artifactId>
    <version>0.0.1</version>
</dependency>

<dependency>
    <groupId>com.qiniu</groupId>
    <artifactId>qiniu-java-sdk</artifactId>
    <version>[7.0.0, 7.2.99]</version>
</dependency>

```
* 添加注解
```
@SpringCloudApplication
@EnableOssClient(provider = Provider.QINIU_OSS)
public class XXXService {

    public static void main(String[] args) {
        SpringApplication.run(XXXService.class, args);
    }
}
```

* 配置sdk参数 

```
oss:
  accessKey: ak
  secretKey: sk
  bucketName: oss空间名称
  url-prefix: 绑定的域名（七牛使用）阿里可以与endpoint相同
  if-private: 是否公开权限
  endpoint: 阿里专用，对应oss的区域域名
  internal-url: 内网访问地址 (可选)

```

> enjoy yourself!!