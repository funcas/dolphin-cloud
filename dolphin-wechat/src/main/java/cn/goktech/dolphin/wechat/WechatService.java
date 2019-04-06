package cn.goktech.dolphin.wechat;

import cn.goktech.dolphin.security.annotation.EnableDolResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月05日
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = "cn.goktech.dolphin.*.feign")
@EnableDolResourceServer
@EnableOAuth2Client
public class WechatService {

    public static void main(String[] args) {
        SpringApplication.run(WechatService.class, args);
    }

}
