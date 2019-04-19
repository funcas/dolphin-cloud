package cn.goktech.dolphin.schedule.annotation;

import cn.goktech.dolphin.schedule.autoconfigure.JobParserAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月19日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({JobParserAutoConfiguration.class})
public @interface EnableElasticJob {
}
