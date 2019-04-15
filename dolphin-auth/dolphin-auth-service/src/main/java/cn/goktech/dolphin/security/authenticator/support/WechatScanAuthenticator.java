package cn.goktech.dolphin.security.authenticator.support;

import cn.goktech.dolphin.security.authenticator.AbstractAuthenticator;
import cn.goktech.dolphin.security.authenticator.IntegrationAuthentication;
import cn.goktech.dolphin.upms.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 微信扫码登陆
 * @author funcas
 * @version 1.0
 * @date 2018年11月28日
 */
@Slf4j
//@Component
public class WechatScanAuthenticator extends AbstractAuthenticator {

    private final static String VERIFICATION_CODE_AUTH_TYPE = "wx_scan";

//    @Autowired
//    private IAccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User authenticate(IntegrationAuthentication integrationAuthentication) {
//        String ticket = integrationAuthentication.getAuthParameter("password");
//        String openid = integrationAuthentication.getAuthParameter("username");
//
//        User user = accountService.findUserByOpenid(openid);
//        if(user != null){
//            user.setPassword(passwordEncoder.encode(ticket));
//        }else{
//            throw new ServiceException("认证异常");
//        }

        return null;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return VERIFICATION_CODE_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}
