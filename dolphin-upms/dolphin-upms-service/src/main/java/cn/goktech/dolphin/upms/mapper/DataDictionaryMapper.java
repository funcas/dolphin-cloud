package cn.goktech.dolphin.upms.mapper;

import cn.goktech.dolphin.upms.entity.DataDictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据字典数据访问
 *
 */
@Mapper
public interface DataDictionaryMapper extends BaseMapper<DataDictionary> {

    IPage<DataDictionary> find(Page<DataDictionary> page, @Param("filter") Map<String, Object> filter);

    /**
     * 判断代码是否唯一
     *
     * @param code 代码
     *
     * @return true 表示唯一，否则 false
     */
    boolean isCodeUnique(@Param("code") String code);

    /**
     * 获取数据字典
     *
     * @param code 字典类别代码
     *
     * @return 数据字典实体 Map
     */
    List<DataDictionary> getByCategoryCode(@Param("code") String code);

    /**
     * 获取数据字典
     *
     * @param code 字典代码
     *
     * @return 数据字典实体 Map
     */
    DataDictionary getByCode(String code);
}
