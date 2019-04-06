package cn.goktech.dolphin.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 全局用户信息
 * @author funcas
 * @version 1.0
 * @date 2019年04月04日
 */
public class DolUserDTO extends User {
    private static final long serialVersionUID = 2212776358383321996L;

    private Long id;
    private Long unitId;
    private String openid;

    public DolUserDTO(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Long unitId, String openid) {
        super(username, password, authorities);
        this.id = id;
        this.unitId = unitId;
        this.openid = openid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
