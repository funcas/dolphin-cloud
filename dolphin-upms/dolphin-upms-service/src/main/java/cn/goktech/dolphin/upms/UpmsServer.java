package cn.goktech.dolphin.upms;

import cn.goktech.dolphin.security.annotation.EnableDolResourceServer;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
@SpringCloudApplication
@EnableDolResourceServer
@EnableTransactionManagement
@EnableCaching
@EnableFeignClients(basePackages = "cn.goktech.dolphin.*.feign")
@EnableOAuth2Client
public class UpmsServer {

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(UpmsServer.class, args);
    }
}
