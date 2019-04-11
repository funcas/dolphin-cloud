package cn.goktech.dolphin.pay.feign;

import cn.goktech.dolphin.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@FeignClient(value = "dolphin-wechat-service")
public interface WechatService {

    @GetMapping("/wechat/demo")
    public ApiResult demo(@RequestParam("nickname") String nickname);
}
