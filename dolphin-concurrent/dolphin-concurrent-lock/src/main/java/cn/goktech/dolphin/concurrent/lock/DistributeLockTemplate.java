package cn.goktech.dolphin.concurrent.lock;

import cn.goktech.dolphin.concurrent.lock.zk.ZkDistributeLock;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月16日
 */
public class DistributeLockTemplate {

    private static final long _DEFAULT_LOCK_HOLD_MILLS = 30000;


    public static <T> T execute(String lockId,LockCaller<T> caller){
        return execute(lockId, caller, _DEFAULT_LOCK_HOLD_MILLS);
    }

    /**
     * @param lockId 要确保不和其他业务冲突（不能用随机生成）
     * @param caller 业务处理器
     * @param timeout 超时时间（毫秒）
     * @return
     */
    public static <T> T execute(String lockId,LockCaller<T> caller,long timeout){
        ZkDistributeLock dLock = new ZkDistributeLock(lockId,(int)timeout/1000);

        boolean getLock = false;
        try {
            if(dLock.tryLock()){
                getLock = true;
                return caller.onHolder();
            }else{
                return caller.onWait();
            }
        } finally {
            if (getLock) {
                dLock.unlock();
            }
        }

    }
}
