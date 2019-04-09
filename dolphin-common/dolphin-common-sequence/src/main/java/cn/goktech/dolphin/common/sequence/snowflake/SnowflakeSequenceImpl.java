package cn.goktech.dolphin.common.sequence.snowflake;

import cn.goktech.dolphin.common.sequence.ISequence;
import cn.goktech.dolphin.common.sequence.util.Toolkit;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.base.Preconditions;

import java.util.Random;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Slf4j
public class SnowflakeSequenceImpl implements ISequence {

    private final long twepoch = 1288834974657L;
    private final long workerIdBits = 10L;
    /**
     * 最大能够分配的workerid =1023
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits;
    private final long sequenceMask = ~(-1L << sequenceBits);
    private long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private boolean initFlag;
    private static final Random RANDOM = new Random();

    public SnowflakeSequenceImpl(String zkAddress, int port, String name, boolean distribute) {
        if (distribute) {
            SnowflakeZookeeperHolder holder = new SnowflakeZookeeperHolder(Toolkit.getIp(),
                    String.valueOf(port), zkAddress, name);
            this.initFlag = holder.init();
            if (initFlag) {
                workerId = holder.getWorkerID();
                log.info("START SUCCESS USE ZK WORKERID-{}", workerId);
            } else {
                log.error("Snowflake Id Gen is not init ok");
            }
        } else {
            // 本地开发环境，直接取0
            workerId = 0;
        }

        Preconditions.checkArgument(workerId >= 0 && workerId <= maxWorkerId, "workerID must gte 0 and lte 1023");
    }

    @Override
    public synchronized Long get() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        return -1L;
                    }
                } catch (InterruptedException e) {
                    log.error("wait interrupted");
                    return -2L;
                }
            } else {
                return -3L;
            }
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //seq 为0的时候表示是下一毫秒时间开始对seq做随机
                sequence = RANDOM.nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果是新的ms开始
            sequence = RANDOM.nextInt(100);
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;

    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public long getWorkerId() {
        return workerId;
    }
}
