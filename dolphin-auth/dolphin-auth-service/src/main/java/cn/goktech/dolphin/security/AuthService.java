package cn.goktech.dolphin.security;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
@SpringCloudApplication
@EnableResourceServer
@EnableFeignClients(basePackages = "cn.goktech.dolphin.*.feign")
public class AuthService {

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthService.class, args);
//        System.out.println(new BCryptPasswordEncoder().encode("app"));
    }
}
