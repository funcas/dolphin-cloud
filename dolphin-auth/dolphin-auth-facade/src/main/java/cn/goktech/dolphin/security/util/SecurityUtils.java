package cn.goktech.dolphin.security.util;

import cn.goktech.dolphin.security.entity.DolUserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月03日
 */
public class SecurityUtils {
    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    public DolUserDTO getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof DolUserDTO) {
            return (DolUserDTO) principal;
        }
        return null;
    }

    /**
     * 获取用户
     */
    public DolUserDTO getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

}
