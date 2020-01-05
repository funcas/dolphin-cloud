package cn.goktech.dolphin.security.conf;

import cn.goktech.dolphin.security.service.ITokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

/**
 * 重写认证成功方法，添加删除同一个人的上次token
 * @author funcas
 * @version 1.0
 * @date 2020年01月05日
 */
@Slf4j
public class LoginAuthenticationProvider extends DaoAuthenticationProvider {

    private ITokenService tokenService;

    public LoginAuthenticationProvider(UserDetailsService userDetailsService, ITokenService tokenService) {
        super();
        // 这个地方一定要对userDetailsService赋值，不然userDetailsService是null
        setUserDetailsService(userDetailsService);
        this.tokenService = tokenService;
    }


    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        Authentication auth = super.createSuccessAuthentication(principal, authentication, user);
        if (authentication.getDetails() != null && authentication.getDetails() instanceof Map) {
            String clientId = ((Map<String,String>)authentication.getDetails()).get("client_id");
            tokenService.removeTokenByClientIdAndUsername(clientId, user.getUsername());
        }
        return auth;
    }
}