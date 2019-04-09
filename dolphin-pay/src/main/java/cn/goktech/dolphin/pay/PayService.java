package cn.goktech.dolphin.pay;

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
@EnableFeignClients
@EnableDolResourceServer
@EnableOAuth2Client
public class PayService {

    public static void main(String[] args) {
        SpringApplication.run(PayService.class, args);
    }
}
