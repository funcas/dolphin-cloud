package cn.goktech.dolphin.upms.entity;


import cn.goktech.dolphin.common.BaseEntity;
import cn.goktech.dolphin.common.enumeration.entity.State;
import cn.goktech.dolphin.upms.VariableUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2018年04月09日
 */
@TableName("tb_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity<Long> {
    public static final String BIZ_ALIAS = "tb_user";
    private static final long serialVersionUID = -2269302169894751895L;
    @Length(max = 64)
    @Email
    private String email;

    private String password;

    @Length(max = 16)
    private String nickname;
    @NotNull
    private Integer state;
    @NotNull
    @Length(max = 16)
    private String username;
    @Length(max = 255)
    private String avatar;
    @Length(max = 16)
    private String phone;
    private int sex;
    @Length(max = 10)
    private String birthday;
    @Length(max = 128)
    private String address;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long unitId;
    @TableField(exist = false)
    private List<Group> groups;
    @TableField(exist = false)
    private Unit organization;
    private String openid;
    @TableField(exist = false)
    private List<String> perms;

//    private transient Set<GrantedAuthority> grantedAuthorities;

    public String getStateName() {
        return VariableUtils.getName(State.class, this.getState());
    }
}
