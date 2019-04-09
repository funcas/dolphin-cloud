package cn.goktech.dolphin.security.service;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
public interface ITokenService {

    public boolean revokeAccessToken(OAuth2Authentication authentication);
}
