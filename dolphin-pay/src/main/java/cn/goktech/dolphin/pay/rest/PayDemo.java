package cn.goktech.dolphin.pay.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.oss.spring.OssProviderSpringFacade;
import cn.goktech.dolphin.pay.feign.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

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
    @Autowired
    private OssProviderSpringFacade ossProviderSpringFacade;

    @GetMapping("/demo")
    public ApiResult payDemo(){
        ossProviderSpringFacade.upload("119edb2a2834349bd6fe6829c3ea15ce34d3bed1",
                new File("/Users/funcas/Pictures/119edb2a2834349bd6fe6829c3ea15ce34d3bed1.jpg"));
        return success("");
    }




}
