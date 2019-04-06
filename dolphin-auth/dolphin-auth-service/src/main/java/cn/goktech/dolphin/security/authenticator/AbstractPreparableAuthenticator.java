package cn.goktech.dolphin.security.authenticator;

import cn.goktech.dolphin.upms.entity.User;

/**
 * @author funcas
 * @version 1.0
 * @date 2018年11月28日
 */
public abstract class AbstractPreparableAuthenticator implements IAuthenticator {

    @Override
    public abstract User authenticate(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract void prepare(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract boolean support(IntegrationAuthentication integrationAuthentication);

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) { }
}
