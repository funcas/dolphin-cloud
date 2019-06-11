package cn.goktech.dolphin.security.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.security.service.ITokenService;
import cn.goktech.dolphin.security.util.SecurityUtils;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月06日
 */

@Slf4j
@RestController
public class AuthController extends BaseController {

    private final ITokenService tokenService;

    private final RestTemplate restTemplate;

    @Autowired
    public AuthController(ITokenService tokenService, RestTemplate restTemplate) {
        this.tokenService = tokenService;
        this.restTemplate = restTemplate;
    }


    /**
     * 登出操作
     * @param
     * @return
     */
    @PostMapping("/doLogout")
    public ApiResult logout() {
        if(log.isInfoEnabled()){
            log.info("do Logout!");
        }
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityUtils.getAuthentication();
        boolean result = tokenService.revokeAccessToken(authentication);
        return success(result);
    }

    /**
     * 用户登录
     * @param params
     * @return
     */
    @Deprecated
    @PostMapping("/logon")
    public ApiResult<OAuth2AccessToken> login(@RequestParam Map<String, String> params, HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
//        map.add("client_id", appid);
//        map.add("client_secret", secret);
        map.add("username", params.get("username"));
        map.add("password", params.get("password"));

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(map, headers);

        String url = request.getScheme() + "://" + request.getRemoteAddr() + ":" + request.getServerPort() + "/oauth/token";
        ResponseEntity<OAuth2AccessToken> ret = restTemplate.postForEntity(url, req, OAuth2AccessToken.class);
        return success(ret.getBody());
    }


    @GetMapping("/demo")
    public ApiResult getDemo(){
        System.out.println(System.currentTimeMillis());
        return success(ImmutableMap.of("a","funcas"));
    }
}
