package cn.goktech.dolphin.security.conf;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.enumeration.ApiResultEnum;
import cn.goktech.dolphin.common.util.FastJsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年06月12日
 */
public class DolAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) {

        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            response.getWriter().write(FastJsonUtil.toJson(ApiResult.builder()
                    .apiResultEnum(ApiResultEnum.UNAUTHORIZED).build()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
