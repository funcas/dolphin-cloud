package cn.goktech.dolphin.upms.rest.inner;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.rest.UserController;
import cn.goktech.dolphin.upms.service.IAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月10日
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController extends UserController {

    public UserInnerController(IAccountService accountService) {
        super(accountService);
    }

    /**
     * 获取用户信息接口
     * @param
     * @return
     */
    @GetMapping("/userinfo")
    public ApiResult<User> getUserInfo(String username){
        User user = accountService.getUserInfo(username);
        return success(user);
    }

}
