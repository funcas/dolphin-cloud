package cn.goktech.dolphin.common.sequence.conf;


import cn.goktech.dolphin.common.sequence.ISequence;
import cn.goktech.dolphin.common.sequence.snowflake.SnowflakeSequenceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Configuration
@Slf4j
public class SeqConfiguration {

    @Value("${seq.zkAddr:}")
    private String zkAddr;
    @Value("${server.port:0}")
    private int port;
    @Value("${spring.application.name:}")
    private String name;
    @Value("${seq.distribute:false}")
    private boolean isDistribute;

    @Bean
    public ISequence sequence() {
        if (StringUtils.isEmpty(zkAddr)) {
            log.warn("Do you forgot to set attribute for sequence?");
        }
        return new SnowflakeSequenceImpl(zkAddr, port, name, isDistribute);
    }
}


