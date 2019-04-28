package cn.goktech.dolphin.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * 全局用户信息
 * @author funcas
 * @version 1.0
 * @date 2019年04月04日
 */
public class DolUserDTO extends User {
    private static final long serialVersionUID = 2212776358383321996L;

    private Long id;
    private Object unit;
    private String openid;
    private List groups;

    public DolUserDTO(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id,
                      Object unit, String openid, List groups) {
        super(username, password, authorities);
        this.id = id;
        this.unit = unit;
        this.openid = openid;
        this.groups = groups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public List getGroups() {
        return groups;
    }

    public void setGroups(List groups) {
        this.groups = groups;
    }
}
