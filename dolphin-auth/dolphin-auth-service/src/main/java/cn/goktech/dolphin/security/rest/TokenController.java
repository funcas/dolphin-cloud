package cn.goktech.dolphin.security.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.security.service.ITokenService;
import cn.goktech.dolphin.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@RestController
@RequestMapping("/token")
public class TokenController extends BaseController {

    private final ITokenService tokenService;

    @Autowired
    public TokenController(ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 清除token, 用于登出操作
     * @param
     * @return
     */
    @PostMapping("/revoke")
    public ApiResult<Boolean> revokeToken(){
        OAuth2Authentication authentication = (OAuth2Authentication)SecurityUtils.getAuthentication();
        boolean result = tokenService.revokeAccessToken(authentication);
        return success(result);
    }


}
