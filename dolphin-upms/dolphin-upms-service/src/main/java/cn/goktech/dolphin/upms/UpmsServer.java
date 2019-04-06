package cn.goktech.dolphin.upms;

import cn.goktech.dolphin.security.annotation.EnableDolResourceServer;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
@SpringCloudApplication
@EnableDolResourceServer
@ComponentScan({"cn.goktech.dolphin.upms", "cn.goktech.dolphin.common.spring"})
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
