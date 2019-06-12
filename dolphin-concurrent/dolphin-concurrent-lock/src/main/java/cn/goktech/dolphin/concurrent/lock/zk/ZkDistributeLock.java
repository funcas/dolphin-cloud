package cn.goktech.dolphin.concurrent.lock.zk;

import cn.goktech.dolphin.concurrent.lock.exception.LockException;
import org.apache.commons.lang3.Validate;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZkDistributeLock implements Lock,Watcher {
	private static final String LOCK_KEY_SUFFIX = "_lk_";
	private static final String ROOT_PATH = "/dlocks";
	private static final int DEFAULT_SESSION_TIMEOUT = 30000;
	private static String zkServers;
	
	static{
//		zkServers = ResourceUtils.getProperty("jeesuite.lock.zkServers");
	}
	
	private ZooKeeper zk;
	/**
	 * 竞争资源的标志
	 */
	private String lockName;
	/**
	 * 等待前一个锁
	 */
	private String waitNode;
	/**
	 * 当前锁
	 */
	private String myZnode;
	/**
	 * 计数器
	 */
	private CountDownLatch latch;
	private int sessionTimeout;

	
	public ZkDistributeLock(String lockName){
		this(lockName, DEFAULT_SESSION_TIMEOUT);
	}
	
	/**
	 *
	 * @param lockName
	 * @param sessionTimeout
	 */
	public ZkDistributeLock(String lockName,int sessionTimeout) {
		
		Validate.notBlank(zkServers, "config[jeesuite.lock.zkServers] not found");
		if(lockName.contains(LOCK_KEY_SUFFIX)){
			throw new LockException("lockName 不能包含[" + LOCK_KEY_SUFFIX + "]");
		}
		this.lockName = lockName;
		this.sessionTimeout = sessionTimeout;
		try {
			zk = new ZooKeeper(zkServers, sessionTimeout, this);
			Stat stat = zk.exists(ROOT_PATH, false);
			if (stat == null) {
				// 创建根节点
				zk.create(ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			throw new LockException(e);
		}
	}

	/**
	 * zookeeper节点的监视器
	 */
	@Override
	public void process(WatchedEvent event) {
		if (this.latch != null) {
			if(event.getType() == EventType.NodeDeleted){				
				this.latch.countDown();
			}
		}
	}

	@Override
	public void lock() {
		try {
			if (this.tryLock()) {
				return;
			} else {
				waitForLock(waitNode, sessionTimeout);
			}
		} catch (KeeperException e) {
			throw new LockException(e);
		} catch (InterruptedException e) {
			throw new LockException(e);
		}
	}

	@Override
	public boolean tryLock() {
		try {
			// 创建临时子节点
			myZnode = zk.create(ROOT_PATH + "/" + lockName + LOCK_KEY_SUFFIX, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			// 取出所有子节点
			List<String> subNodes = zk.getChildren(ROOT_PATH, false);
			if(subNodes.size() == 1){
				System.out.println("get lock");
				return true;
			}
			// 取出所有lockName的锁
			List<String> lockObjNodes = new ArrayList<String>();
			for (String node : subNodes) {
				if (node.split(LOCK_KEY_SUFFIX)[0].equals(lockName)) {
					lockObjNodes.add(node);
				}
			}
			Collections.sort(lockObjNodes);
			// 如果是最小的节点,则表示取得锁
			if (myZnode.equals(ROOT_PATH + "/" + lockObjNodes.get(0))) {
				return true;
			}
			// 如果不是最小的节点，找到比自己小1的节点
			String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
			waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
		} catch (KeeperException e) {
			throw new LockException(e);
		} catch (InterruptedException e) {
			throw new LockException(e);
		}
		return false;
	}


	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		try {
			if (this.tryLock()) {
				return true;
			}
			return waitForLock(waitNode, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean waitForLock(String waitNode, long waitTime) throws InterruptedException, KeeperException {
		Stat stat = zk.exists(ROOT_PATH + "/" + waitNode, true);
		// 判断比自己小一个数的节点是否存在,如果不存在则无需等待锁
		if (stat != null) {
			this.latch = new CountDownLatch(1);
			this.latch.await(waitTime, TimeUnit.MILLISECONDS); 
			this.latch = null;
		}
		return true;
	}

	@Override
	public void unlock() {
		try {
			zk.delete(myZnode, -1);
			myZnode = null;
			zk.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		this.lock();
	}

	@Override
	public Condition newCondition() {
		return null;
	}
}
