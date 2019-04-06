package cn.goktech.dolphin.security.annotation;

import cn.goktech.dolphin.security.conf.FilterIgnorePropertiesConfig;
import cn.goktech.dolphin.security.conf.JwtConfig;
import cn.goktech.dolphin.security.conf.ResourceServerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ResourceServerConfig.class, JwtConfig.class})
@EnableConfigurationProperties(FilterIgnorePropertiesConfig.class)
public @interface EnableDolResourceServer {
}
