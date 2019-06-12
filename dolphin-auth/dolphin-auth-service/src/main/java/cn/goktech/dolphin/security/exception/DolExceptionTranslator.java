package cn.goktech.dolphin.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月12日
 */
@Component
public class DolExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new DolOauthException(e.getMessage()));
    }
}
