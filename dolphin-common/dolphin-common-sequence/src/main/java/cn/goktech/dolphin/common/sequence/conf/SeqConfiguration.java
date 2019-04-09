package cn.goktech.dolphin.common.sequence.conf;


import cn.goktech.dolphin.common.sequence.ISequence;
import cn.goktech.dolphin.common.sequence.snowflake.SnowflakeSequenceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Configuration
public class SeqConfiguration {

    @Value("${seq.zkAddr}")
    private String zkAddr;
    @Value("${server.port}")
    private int port;
    @Value("${spring.application.name}")
    private String name;
    @Value("${seq.distribute}")
    private boolean isDistribute;

    @Bean
    public ISequence sequence() {
        return new SnowflakeSequenceImpl(zkAddr, port, name, isDistribute);
    }
}


