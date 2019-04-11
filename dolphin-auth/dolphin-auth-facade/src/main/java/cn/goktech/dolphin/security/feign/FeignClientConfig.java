package cn.goktech.dolphin.security.feign;

import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月05日
 */
@Configuration
@ConditionalOnClass({ Feign.class })
@ConditionalOnProperty(value = "feign.oauth2.enabled", matchIfMissing = true)
public class FeignClientConfig {

    @Bean
    @ConditionalOnBean(OAuth2ClientContext.class)
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
        return new DolOAuth2FeignRequestInterceptor(oauth2ClientContext);
    }
}
