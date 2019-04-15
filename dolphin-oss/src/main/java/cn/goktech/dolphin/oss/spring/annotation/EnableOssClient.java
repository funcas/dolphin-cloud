package cn.goktech.dolphin.oss.spring.annotation;

import cn.goktech.dolphin.oss.enumeration.Provider;
import cn.goktech.dolphin.oss.spring.conf.OssClientImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({OssClientImportSelector.class})
public @interface EnableOssClient {

    Provider provider();
}
