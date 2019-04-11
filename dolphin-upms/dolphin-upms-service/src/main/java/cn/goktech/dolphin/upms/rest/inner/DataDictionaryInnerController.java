package cn.goktech.dolphin.upms.rest.inner;

import cn.goktech.dolphin.common.ApiResult;
import cn.goktech.dolphin.upms.entity.DataDictionary;
import cn.goktech.dolphin.upms.rest.DataDictionaryController;
import cn.goktech.dolphin.upms.service.ISystemVariableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月10日
 */
@RestController
@RequestMapping("/inner")
public class DataDictionaryInnerController extends DataDictionaryController {

    public DataDictionaryInnerController(ISystemVariableService systemVariableService) {
        super(systemVariableService);
    }

    @GetMapping("/dict/getDataDictionaries")
    public ApiResult<List<DataDictionary>> getDataDictionaries(@RequestParam("code") String code) {
        return success(systemVariableService.getDataDictionaries(code));
    }

    @GetMapping("/dict/getDataDictionary")
    public ApiResult<DataDictionary> getDataDictionary(@RequestParam("code") String code) {
        return success(systemVariableService.getDataDictionary(code));
    }
}
