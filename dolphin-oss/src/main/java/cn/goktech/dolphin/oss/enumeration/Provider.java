package cn.goktech.dolphin.oss.enumeration;

import cn.goktech.dolphin.common.enumeration.ValueEnum;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月15日
 */
public enum Provider implements ValueEnum<String> {

    /**
     * 阿里云
     */
    ALI_OSS("阿里云oss", "aliyun"),
    /**
     * 七牛云
     */
    QINIU_OSS("七牛云oss", "qiniu");

    String name;
    String value;
    private Provider(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

}
