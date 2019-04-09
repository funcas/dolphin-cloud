package cn.goktech.dolphin.upms.feign;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.upms.feign.fallback.RemoteAuthServiceFallback;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@FeignClient(value = "dolphin-auth-service")
public interface RemoteAuthService {

    @PostMapping("/oauth/token")
    public OAuth2AccessToken getAccessToken(@RequestParam Map<String, String> parameters);

    @PostMapping("/token/revoke")
    public ApiResult<Boolean> revokeAccessToken();
}
