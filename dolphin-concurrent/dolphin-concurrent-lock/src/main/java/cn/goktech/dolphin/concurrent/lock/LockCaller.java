package cn.goktech.dolphin.concurrent.lock;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月16日
 */
public interface LockCaller<T> {

    /**
     * 持有锁的操作
     * @return
     */
    T onHolder();

    /**
     * 等待锁的操作
     * @return
     */
    T onWait();
}
