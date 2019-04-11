package cn.goktech.dolphin.security.service.impl;

import cn.goktech.dolphin.security.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Service
public class TokenServiceImpl implements ITokenService {

    private final RedisTokenStore redisTokenStore;

    @Autowired
    public TokenServiceImpl(RedisTokenStore redisTokenStore) {
        this.redisTokenStore = redisTokenStore;
    }

    @Override
    public boolean revokeAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = redisTokenStore.getAccessToken(authentication);
        if (accessToken == null) {
            return false;
        }
        if (accessToken.getRefreshToken() != null) {
            redisTokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        redisTokenStore.removeAccessToken(accessToken);
        return true;
    }

    /**
     *
     * @param clientId
     * @param username
     */
    @Override
    public void removeTokenByClientIdAndUsername(String clientId, String username) {
        Collection<OAuth2AccessToken> tokenList = redisTokenStore.findTokensByClientIdAndUserName(clientId, username);
        for(OAuth2AccessToken token : tokenList) {
            redisTokenStore.removeRefreshToken(token.getRefreshToken());
            redisTokenStore.removeAccessToken(token.getValue());
        }
    }
}
