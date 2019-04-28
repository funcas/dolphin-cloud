package cn.goktech.dolphin.pay;

import cn.goktech.dolphin.oss.enumeration.Provider;
import cn.goktech.dolphin.oss.spring.annotation.EnableOssClient;
import cn.goktech.dolphin.security.annotation.EnableDolResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = "cn.goktech.dolphin.*.feign")
@EnableDolResourceServer
@EnableOAuth2Client
@EnableOssClient(provider = Provider.QINIU_OSS)
//@ComponentScan(basePackages = {"cn.goktech.dolphin.pay"})
public class PayService {

    public static void main(String[] args) {
        SpringApplication.run(PayService.class, args);
    }
}
