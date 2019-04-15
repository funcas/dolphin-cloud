package cn.goktech.dolphin.pay.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.common.sequence.IdTools;
import cn.goktech.dolphin.oss.spring.OssProviderSpringFacade;
import cn.goktech.dolphin.pay.feign.WechatService;
import cn.goktech.dolphin.upms.VariableUtils;
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
    OssProviderSpringFacade facade;


    @GetMapping("/demo")
    public ApiResult payDemo(){
        return wechatService.demo("f");
    }

    @GetMapping("/getKey")
    public ApiResult<String> getKey() {
        String url = facade.upload("09a94d4a20a446232a9021229a22720e0df3d7a7", new File("/Users/funcas/Pictures/09a94d4a20a446232a9021229a22720e0df3d7a7.jpg"));
        return success(url);
    }

    @GetMapping("/getUrl")
    public ApiResult getUrl(String key) {
        return success(facade.getFsProvider().getDownloadUrl(key, false));
    }

    @GetMapping("/getDict")
    public Object getDict(String code) {
        return VariableUtils.getName("STATE", "1");
    }

    @GetMapping("/delPic")
    public Object delPic() {
        return success(facade.delete("09a94d4a20a446232a9021229a22720e0df3d7a7"));
    }


}
