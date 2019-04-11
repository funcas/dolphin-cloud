package cn.goktech.dolphin.upms.feign;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.constants.ServiceNameConstants;
import cn.goktech.dolphin.upms.entity.DataDictionary;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.feign.fallback.RemoteUserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
@FeignClient(value = ServiceNameConstants.UPMS_SERVICE, fallbackFactory = RemoteUserServiceFallback.class)
public interface RemoteUserService {

    @GetMapping("/inner/userinfo")
    ApiResult<User> getUserInfo(@RequestParam("username") String username);

    @GetMapping("/inner/dict/getDataDictionaries")
    ApiResult<List<DataDictionary>> getDataDictionaries(@RequestParam("code") String code);

    @GetMapping("/inner/dict/getDataDictionary")
    ApiResult<DataDictionary> getDataDictionary(@RequestParam("code") String code);

}
