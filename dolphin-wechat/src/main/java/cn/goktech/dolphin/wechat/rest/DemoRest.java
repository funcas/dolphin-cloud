package cn.goktech.dolphin.wechat.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.upms.feign.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月05日
 */
@RestController
@RequestMapping("/wechat")
public class DemoRest {

    @Autowired
    private RemoteUserService remoteUserService;

    @GetMapping("/demo")
    public ApiResult demo(String nickname){
        return remoteUserService.getAllUserLists(nickname);
    }
}
