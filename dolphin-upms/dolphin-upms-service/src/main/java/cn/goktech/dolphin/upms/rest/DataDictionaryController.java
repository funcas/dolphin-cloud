package cn.goktech.dolphin.upms.rest;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.common.PageRequest;
import cn.goktech.dolphin.common.PropertyFilters;
import cn.goktech.dolphin.common.base.BaseController;
import cn.goktech.dolphin.upms.entity.DataDictionary;
import cn.goktech.dolphin.upms.service.ISystemVariableService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据字典管理
 * @author funcas
 * @version 1.0
 * @date 2018年11月02日
 */
@RestController
@RequestMapping("/sys")
public class DataDictionaryController extends BaseController {

    protected final ISystemVariableService systemVariableService;

    @Autowired
    public DataDictionaryController(ISystemVariableService systemVariableService) {
        this.systemVariableService = systemVariableService;
    }

    /**
     * 分页查询所有数据字典列表
     * @param pageRequest
     * @param request
     * @return
     */
    @GetMapping("/dicts")
    @PreAuthorize("hasAuthority('data-dictionary:list')")
    public Object getDicts(PageRequest pageRequest, HttpServletRequest request){
        return success(systemVariableService.findDataDictionaries(pageRequest, PropertyFilters.get(request, true)));
    }

    @GetMapping("/field-types")
    public Object getFieldTypes() {
//        return success(VariableUtils.get(FieldType.class));
        return null;
    }

    /**
     * 保存数据字典
     * @param entity
     * @return
     */
    @PostMapping("/dict")
    @PreAuthorize("hasAuthority('data-dictionary:save')")
    public Object saveDict(@Valid @RequestBody DataDictionary entity){
        systemVariableService.saveDataDictionary(entity);
        return success(entity);
    }

    /**
     * 删除数据字典
     * @param id
     * @return
     */
    @DeleteMapping("/dict/{id}")
    @PreAuthorize("hasAuthority('data-dictionary:delete')")
    public Object delDict(@PathVariable("id") Long id){
        // TODO: 2019-01-02 校验是否被使用 
        systemVariableService.deleteDataDictionaries(Lists.newArrayList(id));
        return success(id);
    }



}
