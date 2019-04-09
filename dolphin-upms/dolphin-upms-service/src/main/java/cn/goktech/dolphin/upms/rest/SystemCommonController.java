package cn.goktech.dolphin.upms.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.common.exception.ServiceException;
import cn.goktech.dolphin.upms.feign.RemoteAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2018年11月09日
 */
@RestController
@Slf4j
public class SystemCommonController extends BaseController {

    private final RemoteAuthService remoteAuthService;

    @Value("${client.appid}")
    private String appid;
    @Value("${client.secret}")
    private String secret;

    @Autowired
    public SystemCommonController(RemoteAuthService remoteAuthService) {
        this.remoteAuthService = remoteAuthService;
    }

    /**
     * 登出操作
     * @param
     * @return
     */
    @PostMapping("/_logout")
    public ApiResult logout() {
        ApiResult<Boolean> result = remoteAuthService.revokeAccessToken();
        return success(result);
    }

    /**
     * 用户登录
     * @param params
     * @return
     */
    @PostMapping("/_login")
    public ApiResult<OAuth2AccessToken> login(@RequestParam Map<String, String> params){
        params.put("grant_type", "password");
        params.put("client_id", appid);
        params.put("client_secret", secret);
        OAuth2AccessToken token = remoteAuthService.getAccessToken(params);
        if(token == null) {
            throw new ServiceException("网络异常");
        }
        return success(token);
    }
}

