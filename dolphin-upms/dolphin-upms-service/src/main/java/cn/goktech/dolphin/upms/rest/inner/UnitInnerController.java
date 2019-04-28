package cn.goktech.dolphin.upms.rest.inner;

import cn.goktech.dolphin.upms.entity.Unit;
import cn.goktech.dolphin.upms.rest.UnitController;
import cn.goktech.dolphin.upms.service.IUnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月23日
 */
@RestController
@RequestMapping("/inner")
public class UnitInnerController extends UnitController {
    public UnitInnerController(IUnitService unitService) {
        super(unitService);
    }

    @GetMapping("/unit/{id}")
    public Unit getUnitById(@PathVariable("id") Long id) {
        return unitService.selectOne(id);
    }
}
