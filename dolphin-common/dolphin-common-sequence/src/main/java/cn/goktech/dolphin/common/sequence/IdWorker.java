package cn.goktech.dolphin.common.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Component
public class IdWorker {

    private static ISequence sequence;

    @Autowired
    public void setSequence(ISequence sequence) {
        IdWorker.sequence = sequence;
    }

    public static Long getId() {
        return sequence.get();
    }

}
