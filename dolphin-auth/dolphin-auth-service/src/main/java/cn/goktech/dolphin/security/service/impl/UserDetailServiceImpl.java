package cn.goktech.dolphin.security.service.impl;

import cn.goktech.dolphin.common.exception.SystemException;
import cn.goktech.dolphin.common.util.CollectionUtils;
import cn.goktech.dolphin.security.authenticator.AuthenticationContext;
import cn.goktech.dolphin.security.authenticator.IAuthenticator;
import cn.goktech.dolphin.security.authenticator.IntegrationAuthentication;
import cn.goktech.dolphin.security.entity.DolUserDTO;
import cn.goktech.dolphin.upms.entity.User;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private List<IAuthenticator> authenticators;

//    @Autowired
//    private ITokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: 2019-04-04 评审是否需要添加缓存
        IntegrationAuthentication integrationAuthentication = AuthenticationContext.get();
        //判断是否是集成登录
        if (integrationAuthentication == null) {
            integrationAuthentication = new IntegrationAuthentication();
        }

        integrationAuthentication.setUsername(username);
        //仅允许登陆一个，开启这段代码，移除上一次生成的token
//        tokenService.removeTokenByClientIdAndUsername(integrationAuthentication.getAuthParameter("client_id"),
//                username);
        User remoteUser = this.authenticate(integrationAuthentication);
        if (remoteUser == null) {
            log.error("用户名密码不正确！");
            throw new SystemException("用户名或密码错误");
        }
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(remoteUser.getPerms())) {
            for (String perm : remoteUser.getPerms()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(perm));
            }
        }

        return new DolUserDTO(remoteUser.getUsername(), remoteUser.getPassword(), grantedAuthorities,
                remoteUser.getId(), remoteUser.getOrganization(), remoteUser.getOpenid(), remoteUser.getGroups());

    }

    private User authenticate(IntegrationAuthentication integrationAuthentication) {
        if (this.authenticators != null) {
            for (IAuthenticator authenticator : authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }

}
