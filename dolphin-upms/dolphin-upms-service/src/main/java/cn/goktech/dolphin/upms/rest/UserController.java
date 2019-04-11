package cn.goktech.dolphin.upms.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.PageRequest;
import cn.goktech.dolphin.common.PropertyFilters;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.security.util.SecurityUtils;
import cn.goktech.dolphin.upms.entity.User;
import cn.goktech.dolphin.upms.service.IAccountService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理rest
 * @author funcas
 * @version 1.0
 * @date 2018年10月12日
 */
@Slf4j
@RestController
@RequestMapping("/sys")
public class UserController extends BaseController {

    protected final IAccountService accountService;

    @Autowired
    public UserController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/user-info")
    public ApiResult<User> getUserInfo(){
        User user = accountService.getUserInfo(SecurityUtils.getUser().getUsername());
        return success(user);
    }

    /**
     * 获取用户列表接口
     * @param pageRequest
     * @param request
     * @return
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('user:list')")
    public ApiResult getUserLists(PageRequest pageRequest, HttpServletRequest request){
        IPage<User> userIPage = accountService.findUsers(pageRequest, PropertyFilters.get(request, true));
        return success(userIPage);
    }

    /**
     * 按昵称获取用户列表
     * @param nickname
     * @return
     */
    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('user:list')")
    public ApiResult getAllUserLists(String nickname){
        List<User> users = accountService.findUsersByNickname(nickname);
        return success(users);
    }

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('user:edit')")
    public ApiResult modifyUser(@PathVariable("userId") Long userId){
        User user = accountService.getUser(userId);
        return success(user);
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('user:save')")
    public ApiResult saveUser(@Valid @RequestBody User user) {
        accountService.saveUser(user);
        return success(user);
    }

    /**
     * 根据用户id删除用户
     * @param userId
     * @return
     */
    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ApiResult deleteUser(@PathVariable("userId") Long userId){
        accountService.deleteUsers(Lists.newArrayList(userId));
        return success(userId);
    }

    /**
     * 批量删除用户（物理）
     * @param ids
     * @return
     */
    @DeleteMapping("/user/batch")
    public ApiResult deleteUserBatch(@RequestBody List<Long> ids){
        accountService.deleteUsers(ids);
        return success(ids);
    }


}
