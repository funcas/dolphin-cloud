package cn.goktech.dolphin.upms.feign;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.feign.fallback.RemoteUserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
@FeignClient(value = "dolphin-upms-server", fallbackFactory = RemoteUserServiceFallback.class)
public interface RemoteUserService {

    @GetMapping("/sys/userinfo")
    ApiResult<User> getUserInfo(@RequestParam("username") String username);

    @GetMapping("/sys/users/all")
    ApiResult<List<User>> getAllUserLists(@RequestParam("nickname") String nickname);
}
