package cn.goktech.dolphin.security.authenticator.support;

import cn.goktech.dolphin.security.authenticator.AbstractAuthenticator;
import cn.goktech.dolphin.security.authenticator.IntegrationAuthentication;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.feign.RemoteUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author funcas
 * @version 1.0
 * @date 2018年11月28日
 */
@Primary
@Component
public class UsernamePasswordAuthenticator extends AbstractAuthenticator {

    @Autowired
    private RemoteUserService remoteUserService;

    @Override
    public User authenticate(IntegrationAuthentication integrationAuthentication) {
        User result = remoteUserService.getUserInfo(integrationAuthentication.getUsername());
        return result;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }
}
