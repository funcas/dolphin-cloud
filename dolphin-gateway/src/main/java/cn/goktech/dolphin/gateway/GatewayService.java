package cn.goktech.dolphin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月04日
 */
@SpringCloudApplication
public class GatewayService {

    public static void main(String[] args) {
        SpringApplication.run(GatewayService.class, args);
    }
}
