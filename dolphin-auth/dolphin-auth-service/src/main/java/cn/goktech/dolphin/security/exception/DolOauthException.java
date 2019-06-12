package cn.goktech.dolphin.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月12日
 */
@JsonSerialize(using = DolOauthExceptionSerializer.class)
public class DolOauthException extends OAuth2Exception {

    public DolOauthException(String msg) {
        super(msg);
    }
}
