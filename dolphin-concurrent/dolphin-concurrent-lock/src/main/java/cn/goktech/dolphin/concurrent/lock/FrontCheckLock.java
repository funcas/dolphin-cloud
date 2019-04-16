package cn.goktech.dolphin.concurrent.lock;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本地前置检查锁
 * @author funcas
 * @version 1.0
 * @date 2019年04月16日
 */
public class FrontCheckLock extends ReentrantLock {

    private static final long serialVersionUID = 1L;

    private static FrontCheckLock context = new FrontCheckLock();

    private Map<String,FrontCheckLock> localLocks = Maps.newConcurrentMap();

    private String name;
    private AtomicInteger count  = new AtomicInteger(0);

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public AtomicInteger getCount() {
        return count;
    }
    public int updateCount(int delta) {
        return this.count.addAndGet(delta);
    }

    public static boolean lock(String lockName,long timeout, TimeUnit unit){
        FrontCheckLock lc = context.localLocks.get(lockName);
        if(lc == null){
            synchronized (context.localLocks) {
                lc = context.localLocks.get(lockName);
                if(lc == null){
                    lc = new FrontCheckLock();
                    context.localLocks.put(lockName, lc);
                }
            }
        }
        lc.updateCount(1);

        try {
            return lc.tryLock(timeout, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static void unlock(String lockName){
        FrontCheckLock lc = context.localLocks.get(lockName);
        if(lc != null){
            lc.unlock();
            if(lc.updateCount(-1) == 0){
                context.localLocks.remove(lockName);
                lc = null;
            }
        }
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FrontCheckLock other = (FrontCheckLock) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
