package cn.goktech.dolphin.security.conf;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.enumeration.ApiResultEnum;
import cn.goktech.dolphin.common.util.FastJsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月12日
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws ServletException {
        Throwable cause = authException.getCause();

        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            if(cause instanceof InvalidTokenException) {
                response.getWriter().write(FastJsonUtil.toJson(
                        ApiResult.builder().apiResultEnum(ApiResultEnum.TOKEN_INVALID).build()));
            }else{
                response.getWriter().write(FastJsonUtil.toJson(
                        ApiResult.builder().apiResultEnum(ApiResultEnum.UNAUTHORIZED).build()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
