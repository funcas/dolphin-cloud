package cn.goktech.dolphin.security.service.impl;

import cn.goktech.dolphin.security.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Service
public class TokenServiceImpl implements ITokenService {

    private final RedisTokenStore tokenStore;

    @Autowired
    public TokenServiceImpl(RedisTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean revokeAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);
        if (accessToken == null) {
            return false;
        }
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        return true;
    }
}
