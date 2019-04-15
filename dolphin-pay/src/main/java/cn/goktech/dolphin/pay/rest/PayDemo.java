package cn.goktech.dolphin.pay.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.pay.feign.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@RestController
@RequestMapping("/pay")
public class PayDemo extends BaseController {

    @Autowired
    private WechatService wechatService;


    @GetMapping("/demo")
    public ApiResult payDemo(){
        return wechatService.demo("f");
    }




}
