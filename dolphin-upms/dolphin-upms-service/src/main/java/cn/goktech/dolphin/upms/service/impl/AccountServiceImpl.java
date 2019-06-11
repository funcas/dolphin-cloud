package cn.goktech.dolphin.upms.service.impl;

import cn.goktech.dolphin.common.PageRequest;
import cn.goktech.dolphin.common.enumeration.entity.ResourceType;
import cn.goktech.dolphin.common.exception.ServiceException;
import cn.goktech.dolphin.common.sequence.IdTools;
import cn.goktech.dolphin.common.util.CollectionUtils;
import cn.goktech.dolphin.security.constants.AuthConstants;
import cn.goktech.dolphin.security.util.SecurityUtils;
import cn.goktech.dolphin.upms.VariableUtils;
import cn.goktech.dolphin.upms.entity.*;
import cn.goktech.dolphin.upms.mapper.GroupMapper;
import cn.goktech.dolphin.upms.mapper.ResourceMapper;
import cn.goktech.dolphin.upms.mapper.UserMapper;
import cn.goktech.dolphin.upms.service.BaseBizService;
import cn.goktech.dolphin.upms.service.IAccountService;
import cn.goktech.dolphin.upms.service.IUnitService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2018年10月11日
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AccountServiceImpl extends BaseBizService implements IAccountService {

    private final UserMapper userMapper;
    private final GroupMapper groupMapper;
    private final ResourceMapper resourceMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private IUnitService unitService;

    @Autowired
    public AccountServiceImpl(UserMapper userMapper, GroupMapper groupMapper, ResourceMapper resourceMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.groupMapper = groupMapper;
        this.resourceMapper = resourceMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 获取用户
     *
     * @param id 用户主键 ID
     * @return 用户实体 Map
     */
    @Override
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User entity) {

        if (entity.getId() != null) {
            updateUser(entity);
        } else {
            entity.setId(IdTools.getId());
            insertUser(entity);
        }
    }

    /**
     * 新增用户
     *
     * @param entity   用户实体 Map
     */
    private void insertUser(User entity) {

        if (!isUsernameUnique(entity.getUsername())) {
            throw new ServiceException("登录账户[" + entity.getUsername() + "]已存在");
        }

        String encryptPassword = AuthConstants.AUTH_TYPE + passwordEncoder.encode(entity.getPassword());
        entity.setPassword(encryptPassword);
        entity.setId(IdTools.getId());
        userMapper.insert(entity);

        if (CollectionUtils.isNotEmpty(entity.getGroups())) {
            Long id = entity.getId();
            userMapper.insertGroupAssociation(id, entity.getGroups());
        }
    }

    /**
     * 更新用户
     *
     * @param entity   用户实体 Map
     *
     */
    private void updateUser(User entity) {

        Long id = entity.getId();
        if(entity.getPassword() != null && !"".equals(entity.getPassword())) {
            entity.setPassword(AuthConstants.AUTH_TYPE + passwordEncoder.encode(entity.getPassword()));
        }
        userMapper.updateById(entity);

        if (CollectionUtils.isNotEmpty(entity.getGroups())) {
            userMapper.deleteGroupAssociation(id);
            userMapper.insertGroupAssociation(id, entity.getGroups());
        }
    }

    /**
     * 更新用户密码
     *
     * @param entity      用户实体 Map
     * @param oldPassword 当前密码
     * @param newPassword 新密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPassword(User entity, String oldPassword, String newPassword) {


        if (!passwordEncoder.matches(entity.getPassword(), passwordEncoder.encode(oldPassword))) {
            throw new ServiceException("当前密码不正确");
        }

        if (StringUtils.isEmpty(newPassword)) {
            throw new ServiceException("新密码不能为空");
        }
        userMapper.updatePassword(entity.getId(), AuthConstants.AUTH_TYPE + passwordEncoder.encode(newPassword));
    }


    /**
     * 删除用户
     *
     * @param ids 用户主键 ID 集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(List<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        for (Long id : ids) {
            User entity = getUser(id);
            deleteUser(entity);
        }
    }

    @Override
    public List<User> findUsersByOrgId(Long orgId) {
        return userMapper.findUserByOrgId(orgId);
    }

    @Override
    public User findUserByOpenid(String openId) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("openid", openId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOpenidByUserId(User user) {
        userMapper.updateById(user);
    }


    private boolean isSuperAdmin(Long id) {
        DataDictionary data = VariableUtils.getDataDictionary("SYSTEM_VAR_ROLE_ID");
        List<Group> groups = getUserGroups(id);

        for (Group g : groups) {
            if (VariableUtils.cast(data.getValue(),Long.class).equals(g.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除用户
     *
     * @param entity 用户实体 Map
     */
    @Override
    public void deleteUser(User entity) {

        Long id = entity.getId();

        if (isSuperAdmin(id)) {
            throw new ServiceException("这是管理员帐号，不允许删除");
        }

        userMapper.deleteGroupAssociation(id);
        userMapper.deleteById(id);
    }

    /**
     * 获取用户
     *
     * @param username 登录帐号
     *
     * @return 用户实体 Map
     */
    @Override
//    @Cacheable(value = "userCache", key = "#username")
    public User getUserByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    /**
     * 判断用户登录帐号是否唯一
     *
     * @param username 登录帐号
     * @return true 表示唯一，否则 false
     */
    @Override
    public boolean isUsernameUnique(String username) {
        return getUserByUsername(username) == null;
    }

    /**
     * 判断用户电子邮件是否唯一
     *
     * @param email 电子邮件
     *
     * @return true 表示唯一，否则 false
     */
    @Override
    public boolean isUserEmailUnique(String email) {
        return getUserByUsername(email) == null;
    }


    /**
     * 查询用户
     *
     * @param filter      查询条件
     *
     * @return 用户实体 Map 的分页对象
     */
    @Override
    public IPage<User> findUsers(PageRequest pageRequest, Map<String, Object> filter) {
        processDataScopeFilter(filter, User.BIZ_ALIAS);
        return userMapper.find(new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize()), filter);
    }

    @Override
    public List<User> findUsersByNickname(String nickname) {
        return userMapper.findUsersByNickname(nickname);
    }

    @Override
    public User getUserInfo(String username) {
        User user = this.getUserByUsername(username);
        if(user == null){
            throw new ServiceException("用户不存在");
        }
        //todo 这样写不会走缓存，要注意
        List<Resource> resources = this.getUserResources(user.getId());
        List<String> perms = Lists.newArrayList();
        for(Resource resource : resources) {
            if(StringUtils.isNotEmpty(resource.getPermission())) {
                perms.add(resource.getPermission());
            }
        }
        user.setPerms(perms);
        Unit unit = unitService.selectOne(user.getUnitId());
        user.setOrganization(unit);
        user.setGroups(this.getUserGroups(user.getId()));
        return user;
    }

    //----------------------------------- 组管理 ----------------------------------------//

    /**
     * 获取组
     *
     * @param id 组主键 ID
     *
     * @return 组实体 Map
     */
    @Override
    public Group getGroup(Long id) {
        return groupMapper.selectById(id);
    }

    /**
     * 获取用户所在的组
     *
     * @param userId 用户主键 ID
     *
     * @return 组实体 Map 集合
     */
    @Override
    public List<Group> getUserGroups(Long userId) {
        return groupMapper.getUserGroups(userId);
    }

    /**
     * 删除组
     *
     * @param ids 组主键 ID 集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroups(List<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        for (Long id : ids) {
            groupMapper.deleteResourceAssociation(id);
            groupMapper.deleteUserAssociation(id);
            groupMapper.deleteById(id);
        }
    }

    /**
     * 保存组
     *
     * @param entity      组实体 Map
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "userResourceCache", allEntries = true)
    public void saveGroup(Group entity) {
        if (entity.getId() != null) {
            updateGroup(entity);
        } else {
            entity.setId(IdTools.getId());
            insertGroup(entity);
        }
    }

    /**
     * 更新组
     *
     * @param entity      组实体 Map
     */
    private void updateGroup(Group entity) {
        Long id = entity.getId();
        List<Long> unitIds = entity.getUnitIds();
        List<Long> resourceIds = entity.getResourceIds();
        groupMapper.updateById(entity);

        groupMapper.deleteResourceAssociation(id);
        groupMapper.deleteUnitAssociation(id);
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            groupMapper.insertResourceAssociation(id, resourceIds);
        }
        if (CollectionUtils.isNotEmpty(unitIds)) {
            groupMapper.insertUnitAssociation(id, unitIds);
        }
    }

    /**
     * 新增组
     *
     * @param entity      组实体 Map
     */
    private void insertGroup(Group entity) {
        String name = entity.getName();
        List<Long> resourceIds = entity.getResourceIds();
        List<Long> unitIds = entity.getUnitIds();
        if (!isGroupNameUnique(name)) {
            throw new ServiceException("组名称[" + name + "]已存在");
        }
        entity.setUnitId(((Unit)SecurityUtils.getUser().getUnit()).getId());
        groupMapper.insert(entity);
        Long id = entity.getId();

        if (CollectionUtils.isNotEmpty(resourceIds)) {
            groupMapper.insertResourceAssociation(id, resourceIds);
        }

        if (CollectionUtils.isNotEmpty(unitIds)) {
            groupMapper.insertUnitAssociation(id, unitIds);
        }
    }

    /**
     * 判断组名称是否存在
     *
     * @param name 组名称
     *
     * @return ture 表示存在，否则 false。
     */
    @Override
    public boolean isGroupNameUnique(String name) {
        return !groupMapper.isNameUnique(name);
    }
    /**
     * 查询组
     *
     * @param filter 查询条件
     *
     * @return 组实体 Map 的分页对象
     */
    @Override
    public IPage<Group> findGroups(PageRequest pageRequest, Map<String,Object> filter) {
        processDataScopeFilter(filter, Group.GROUP_ALIAS);
        if (isSuperAdmin()) {
            filter.put("superAdmin", "1");
        }
        return groupMapper.find(new Page<>(pageRequest.getPageNumber(),pageRequest.getPageSize()), filter);
    }

    //----------------------------------- 资源管理 ----------------------------------------//

    /**
     * 获取资源
     *
     * @param id 资源主键 ID
     *
     * @return 资源实体 Map
     */
    @Override
    public Resource getResource(Long id) {
        return resourceMapper.selectById(id);
    }


    /**
     * 获取所有资源
     *
     * @return 资源实体 Map 集合
     */
    @Override
    public List<Resource> getResources(Long... ignore) {
        return resourceMapper.getAll(ignore);
    }

    /**
     * 获取用户资源
     *
     * @param userId 用户主键 ID
     *
     * @return 资源实体 Map 集合
     */
    @Override
    @Cacheable(value = "userResourceCache", key = "#userId")
    public List<Resource> getUserResources(Long userId) {
        return resourceMapper.getUserResources(userId);
    }

    /**
     * 获取组资源
     *
     * @param groupId 组主键 ID
     *
     * @return 资源实体 Map 集合
     */
    @Override
    public List<Resource> getGroupResources(Long groupId) {
        return resourceMapper.getGroupResources(groupId);
    }


    /**
     * 删除资源
     *
     * @param ids 资源主键 ID 集合
     */
    @Override
    @Transactional
    public void deleteResources(List<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        for (Long id : ids) {
            deleteResource(id);
        }
    }

    /**
     * 删除资源
     *
     * @param id 资源主键 ID
     */
    private void deleteResource(Long id) {
        List<Resource> children = resourceMapper.getChildren(id);

        if (!children.isEmpty()) {

            for (Resource c : children) {
                Long temp = c.getId();
                deleteResource(temp);
            }

        }

        resourceMapper.deleteGroupAssociation(id);
        resourceMapper.deleteById(id);
    }

    /**
     * 保存资源
     *
     * @param entity 资源实体 Map
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveResource(Resource entity) {

        if ("".equals(entity.getFkParentId())) {
            entity.setFkParentId(null);
        }

        if (entity.getSort() == null || "".equals(entity.getSort())) {
            entity.setSort(resourceMapper.selectCount(new QueryWrapper<>()));
        }

        if (entity.getId() != null) {
            resourceMapper.updateById(entity);
        } else {
            entity.setId(IdTools.getId());
            resourceMapper.insert(entity);
        }
    }

    /**
     * 合并资源，要获取资源的父类通过 "parent" key 来获取，如果不存在 "parent" key 表示该资源 Map 为根节点，
     * 要获取资源的子类通过 "children" key 来获取
     *
     * @param resources 要合并的资源实体 Map 集合
     *
     * @return 合并好的树形资源实体 Map 集合
     */
    @Override
    public List<Resource> mergeResources(List<Resource> resources) {
        return mergeResources(resources, null);
    }

    /**
     * 合并资源，要获取资源的父类通过 "parent" key 来获取，如果不存在 "parent" key 表示该资源 Map 为根节点，
     * 要获取资源的子类通过 "children" key 来获取
     *
     * @param resources  要合并的资源实体 Map 集合
     * @param ignoreType 要忽略资源类型
     *
     * @return 合并好的树形资源实体 Map 集合
     */
    @Override
    public List<Resource> mergeResources(List<Resource> resources, ResourceType ignoreType) {

        List<Resource> result = Lists.newArrayList();

        for (Resource entity : resources) {

            Long parentId = entity.getFkParentId();
            Integer type = entity.getType();

            if (parentId == null && (ignoreType == null || !ignoreType.getValue().equals(type))) {
                recursionResource(entity, resources, ignoreType);
                result.add(entity);
            }

        }

        return result;
    }

    /**
     * 递归并合并资源到指定的父类
     *
     * @param parent     父类
     * @param resources  资源实体 Map 集合
     * @param ignoreType 要忽略不合并的资源类型
     */
    private void recursionResource(Resource parent,
                                   List<Resource> resources,
                                   ResourceType ignoreType) {

        for (Resource entity : resources) {
            Long parentId = entity.getFkParentId();

            if (parentId == null) {
                continue;
            }

            Integer type = entity.getType();
            Long id = parent.getId();

            if ((ignoreType == null || !ignoreType.getValue().equals(type)) && parentId.equals(id)) {
                recursionResource(entity, resources, ignoreType);
                List<Resource> children = parent.getChildren();
                if (children != null) {
                    entity.setParent(parent);
                    children.add(entity);
                }
            }
        }
    }

    @Override
    public List<Resource> getResourcesByGroupId(Long groupId) {
        return mergeResources(resourceMapper.selectGroupResources(groupId));
    }

    @Override
    public List<String> getCheckedResourceIds(Long groupId) {
        return resourceMapper.getResourceByGroupIds(groupId);
    }
}
