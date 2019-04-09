package cn.goktech.dolphin.upms.feign.fallback;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.enumeration.ApiResultEnum;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.feign.RemoteUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月06日
 */
@Slf4j
public class RemoteUserServiceFallback implements FallbackFactory<RemoteUserService> {

    private static final String ERR_MSG = "UPMS 接口暂时不可用: ";

    @Override
    public RemoteUserService create(Throwable throwable) {
        String msg = throwable == null ? "" : throwable.getMessage();
        log.error(msg);
        return new RemoteUserService() {

            @Override
            public ApiResult<User> getUserInfo(String username) {
                return ApiResult.<User>builder()
                        .retCode(ApiResultEnum.UNKNOWN_EXCEPTION.getValue())
                        .retMessage(ERR_MSG)
                        .build();
            }

            @Override
            public ApiResult<List<User>> getAllUserLists(String nickname) {
                return ApiResult.<List<User>>builder()
                        .retCode(ApiResultEnum.UNKNOWN_EXCEPTION.getValue())
                        .retMessage(ERR_MSG)
                        .build();
            }
        };

    }
}
