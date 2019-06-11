package cn.goktech.dolphin.upms.feign.fallback;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.enumeration.ApiResultEnum;
import cn.goktech.dolphin.upms.entity.DataDictionary;
import cn.goktech.dolphin.upms.entity.Unit;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.feign.RemoteUserService;
import com.google.common.collect.Lists;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

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
        log.error(throwable.getMessage());
        return new RemoteUserService() {

            @Override
            public User getUserInfo(String username) {
                return null;
            }

            @Override
            public ApiResult<List<DataDictionary>> getDataDictionaries(String code) {
                return ApiResult.<List<DataDictionary>>builder()
                        .retCode(ApiResultEnum.UNKNOWN_EXCEPTION.getValue())
                        .result(Lists.newArrayList())
                        .retMessage(ERR_MSG)
                        .build();
            }

            @Override
            public ApiResult<DataDictionary> getDataDictionary(String code) {
                return ApiResult.<DataDictionary>builder()
                        .retCode(ApiResultEnum.UNKNOWN_EXCEPTION.getValue())
                        .result(new DataDictionary())
                        .retMessage(ERR_MSG)
                        .build();
            }

            @Override
            public Unit getUnitById(Long id) {
                return new Unit();
            }
        };

    }
}
