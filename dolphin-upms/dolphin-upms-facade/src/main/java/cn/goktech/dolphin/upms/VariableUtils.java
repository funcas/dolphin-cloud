package cn.goktech.dolphin.upms;

import cn.goktech.dolphin.common.enumeration.FieldType;
import cn.goktech.dolphin.common.enumeration.ValueEnum;
import cn.goktech.dolphin.common.util.ConvertUtils;
import cn.goktech.dolphin.upms.entity.DataDictionary;
import cn.goktech.dolphin.upms.feign.RemoteUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月10日
 */
@Component
public class VariableUtils {

    /**
     * 默认字典值的值名称
     */
    public static final String DEFAULT_VALUE_NAME = "name";
    /**
     * 默认字典值的键名称
     */
    public static final String DEFAULT_KEY_NAME = "value";

    static public String DEFAULT_DICTIONARY_VALUE = "无";

    /**
     * 系统变量业务逻辑
     */
    private static RemoteUserService remoteUserService;

    /**
     * 设置系统变量业务逻辑
     *
     * @param remoteUserService 系统变量业务逻辑类
     */
    @Autowired
    public void setRemoteUserService(RemoteUserService remoteUserService) {
        VariableUtils.remoteUserService = remoteUserService;
    }

    /**
     * 通过{@link ValueEnum} 接口子类 class 获取数据字典集合
     *
     * @param enumClass 枚举 class
     * @param ignore    要忽略的值
     *
     * @return key value 数据字典 Map 集合
     */
    public static List<Map<String, Object>> get(Class<? extends Enum<? extends ValueEnum<?>>> enumClass, Object... ignore) {

        List<Map<String, Object>> result = Lists.newArrayList();
        Enum<? extends ValueEnum<?>>[] values = enumClass.getEnumConstants();

        for (Enum<? extends ValueEnum<?>> o : values) {
            ValueEnum<?> ve = (ValueEnum<?>) o;
            Object value = ve.getValue();

            if (!ArrayUtils.contains(ignore, value)) {
                Map<String, Object> dictionary = Maps.newHashMap();

                dictionary.put(DEFAULT_VALUE_NAME, value);
                dictionary.put(DEFAULT_KEY_NAME, ve.getName());

                result.add(dictionary);
            }

        }

        return result;
    }

    /**
     * 通过字典类别获取数据字典集合
     *
     * @param code   字典类别代码
     * @param ignore 要忽略的值
     *
     * @return key value 数据字典 Map 集合
     */
    public static List<Map<String,Object>> get(String code, Object... ignore) {
        List<Map<String,Object>> result = Lists.newArrayList();
        List<DataDictionary> dataDictionaries = remoteUserService.getDataDictionaries(code).getResult();

        for (DataDictionary data : dataDictionaries) {
            String value = data.getValue();
            if (ArrayUtils.contains(ignore, value)) {
                continue;
            }
            String type = data.getType();

            Map<String, Object> dictionary = Maps.newHashMap();

            dictionary.put(DEFAULT_VALUE_NAME, data.getName());
            dictionary.put(DEFAULT_KEY_NAME, cast(value, type));
            result.add(dictionary);

        }

        return result;
    }

    public static DataDictionary getDataDictionary(String code) {
        return remoteUserService.getDataDictionary(code).getResult();
    }

    /**
     * 通过字典枚举获取字典名称
     *
     * @param enumClass 字典枚举class
     * @param value 值
     *
     * @return String
     */
    public static String getName(Class<? extends Enum<? extends ValueEnum<?>>> enumClass,Object value) {

        if (value == null || enumClass == null) {
            return DEFAULT_DICTIONARY_VALUE;
        }

        if (value instanceof String && StringUtils.isEmpty(value.toString())) {
            return DEFAULT_DICTIONARY_VALUE;
        }

        Enum<?>[] values = enumClass.getEnumConstants();

        for (Enum<?> o : values) {
            ValueEnum<?> ve = (ValueEnum<?>) o;

            if (StringUtils.equals(ve.getValue().toString(), value.toString())) {
                return ve.getName();
            }

        }

        return DEFAULT_DICTIONARY_VALUE;
    }

    /**
     * 获取数据字典名称
     *
     * @param code 类别代码
     * @param value 值
     *
     * @return String
     */
    public static String getName(String code,Object value) {

        if (value == null || code == null) {
            return "";
        }

        if (value instanceof String && StringUtils.isEmpty(value.toString())) {
            return "";
        }

        List<DataDictionary> dataDictionaries = remoteUserService.getDataDictionaries(code).getResult();

        for (DataDictionary dataDictionary : dataDictionaries) {
            if (StringUtils.equals(dataDictionary.getValue(), value.toString())) {
                return dataDictionary.getName();
            }
        }
        return "";
    }

    /**
     * 将值转换为对应的类型
     *
     * @param value 值
     *
     * @return 转换好的值
     */
    public static <T> T cast(Object value) {
        return value == null ? null : (T) value;
    }

    /**
     * 将值转换为对应的类型
     *
     * @param value     值
     * @param typeClass 类型 Class
     *
     * @return 转换好的值
     */
    public static <T> T cast(Object value, Class<T> typeClass) {
        return (T) (value == null ? null : ConvertUtils.convert(value, typeClass));
    }

    /**
     * 将 String 类型的值转换为对应的类型
     *
     * @param value     值
     * @param className 类名称,参考{@link FieldType}
     *
     * @return 转换好的值
     */
    public static <T> T cast(String value, String className) {
        Class<?> type = FieldType.valueOf(className).getValue();
        return (T) cast(value, type);
    }
}
