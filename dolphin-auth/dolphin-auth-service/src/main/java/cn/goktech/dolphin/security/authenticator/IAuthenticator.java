package cn.goktech.dolphin.security.authenticator;

import cn.goktech.dolphin.upms.entity.User;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月06日
 */
public interface IAuthenticator {

    /**
     * 处理集成认证
     * @param integrationAuthentication
     * @return
     */
    User authenticate(IntegrationAuthentication integrationAuthentication);


    /**
     * 进行预处理
     * @param integrationAuthentication
     */
    void prepare(IntegrationAuthentication integrationAuthentication);

    /**
     * 判断是否支持集成认证类型
     * @param integrationAuthentication
     * @return
     */
    boolean support(IntegrationAuthentication integrationAuthentication);

    /** 认证结束后执行
     * @param integrationAuthentication
     */
    void complete(IntegrationAuthentication integrationAuthentication);
}
