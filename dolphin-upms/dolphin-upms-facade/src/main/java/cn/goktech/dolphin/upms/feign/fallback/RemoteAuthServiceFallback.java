package cn.goktech.dolphin.upms.feign.fallback;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.enumeration.ApiResultEnum;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.feign.RemoteAuthService;
import cn.goktech.dolphin.upms.feign.RemoteUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Slf4j
public class RemoteAuthServiceFallback implements FallbackFactory<RemoteAuthService> {
    @Override
    public RemoteAuthService create(Throwable throwable) {
        log.error(null, throwable);
        return new RemoteAuthService() {

            @Override
            public OAuth2AccessToken getAccessToken(Map<String, String> parameters) {
                return null;
            }

            @Override
            public ApiResult<Boolean> revokeAccessToken() {
                return ApiResult.<Boolean>builder()
                        .retCode(ApiResultEnum.UNKNOWN_EXCEPTION.getValue())
                        .retMessage("")
                        .result(false)
                        .build();
            }
        };
    }
}
