package cn.goktech.dolphin.upms.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.PageRequest;
import cn.goktech.dolphin.common.PropertyFilters;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.upms.entity.DictionaryCategory;
import cn.goktech.dolphin.upms.service.ISystemVariableService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 字典类别管理
 * @author funcas
 * @version 1.0
 * @date 2018年11月02日
 */
@RestController
@RequestMapping("/sys")
public class DictionaryCategoryController extends BaseController {

    private final ISystemVariableService systemVariableService;

    @Autowired
    public DictionaryCategoryController(ISystemVariableService systemVariableService) {
        this.systemVariableService = systemVariableService;
    }

    /**
     * 分页查询字典类别
     * @param pageRequest
     * @param request
     * @return
     */
    @GetMapping("/dict-categories")
    @PreAuthorize("hasAuthority('dictionary-category:list')")
    public ApiResult getDictCategorys(PageRequest pageRequest, HttpServletRequest request){
        return success(systemVariableService.findDictionaryCategories(pageRequest, PropertyFilters.get(request, true)));
    }

    /**
     * 更新或保存字典类别
     * @param entity
     * @return
     */
    @PostMapping("/dict-category")
    @PreAuthorize("hasAuthority('dictionary-category:save')")
    public ApiResult saveDictCategory(@Valid @RequestBody DictionaryCategory entity){
        systemVariableService.saveDictionaryCategory(entity);
        return success(entity);
    }

    /**
     * 删除字典类别
     * @param id
     * @return
     */
    @DeleteMapping("/dict-category/{id}")
    @PreAuthorize("hasAuthority('dictionary-category:delete')")
    public ApiResult deleteCategoryById(@PathVariable("id") Long id){
        // TODO: 2019-01-02 校验是否下挂项目 
        systemVariableService.deleteDictionaryCategories(Lists.newArrayList(id));
        return success(id);
    }
}
