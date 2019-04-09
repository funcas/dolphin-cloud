package cn.goktech.dolphin.common.sequence.snowflake;

import cn.goktech.dolphin.common.sequence.exception.CheckLastTimeException;
import cn.goktech.dolphin.common.util.FastJsonUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月08日
 */
@Slf4j
public class SnowflakeZookeeperHolder {

    private Charset charset = Charset.forName("UTF-8");
    private String zkAddressNode;
    private String listenAddress;
    private int workerID;
    private String propPath;
    private String foreverPath;
    private String ip;
    private String port;
    private String connectionString;
    private long lastUpdateTime;

    public SnowflakeZookeeperHolder(String ip, String port, String connectionString, String appName) {
        this.ip = ip;
        this.port = port;
        this.listenAddress = ip + ":" + port;
        this.connectionString = connectionString;
        this.propPath = System.getProperty("java.io.tmpdir") + File.separator + appName  + "/conf/{port}/workerID.properties";
        this.foreverPath = "/snowflake/" + appName + "/forever";
    }

    public boolean init() {
        try {
            CuratorFramework curator =
                    createWithOptions(connectionString,
                            new RetryUntilElapsed(1000, 500),
                            10000, 10000);
            curator.start();
            Stat stat = curator.checkExists().forPath(foreverPath);
            if (stat == null) {
                //不存在根节点,机器第一次启动,创建/snowflake/ip:port-000000000,并上传数据
                zkAddressNode = createNode(curator);
                //worker id 默认是0
                updateLocalWorkerID(workerID);
                //定时上报本机时间给forever节点
                scheduledUploadData(curator, zkAddressNode);
                return true;
            } else {
                Map<String, Integer> nodeMap = Maps.newHashMap();
                Map<String, String> realNode = Maps.newHashMap();
                //存在根节点,先检查是否有属于自己的根节点
                List<String> keys = curator.getChildren().forPath(foreverPath);
                for (String key : keys) {
                    String[] nodeKey = key.split("-");
                    realNode.put(nodeKey[0], key);
                    nodeMap.put(nodeKey[0], Integer.parseInt(nodeKey[1]));
                }
                Integer workerid = nodeMap.get(listenAddress);
                if (workerid != null) {
                    //有自己的节点,zkAddressNode=ip:port
                    zkAddressNode = foreverPath + "/" + realNode.get(listenAddress);
                    workerID = workerid;
                    if (!checkInitTimeStamp(curator, zkAddressNode)) {
                        throw new CheckLastTimeException("init timestamp check error,forever node timestamp gt this node time");
                    }
                    //准备创建临时节点
                    doService(curator);
                    updateLocalWorkerID(workerID);
                    log.info("[Old NODE]find forever node have this endpoint ip-{} port-{} workid-{} childnode and start SUCCESS", ip, port, workerID);
                } else {
                    //表示新启动的节点,创建持久节点 ,不用check时间
                    String newNode = createNode(curator);
                    zkAddressNode = newNode;
                    String[] nodeKey = newNode.split("-");
                    workerID = Integer.parseInt(nodeKey[1]);
                    doService(curator);
                    updateLocalWorkerID(workerID);
                    log.info("[New NODE]can not find node on forever node that endpoint ip-{} port-{} workid-{},create own node on forever node and start SUCCESS ", ip, port, workerID);
                }
            }
        } catch (Exception e) {
            log.error("Start node ERROR {}", e);
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File(propPath.replace("{port}", port + ""))));
                workerID = Integer.valueOf(properties.getProperty("workerID"));
                log.warn("START FAILED ,use local node file properties workerID-{}", workerID);
            } catch (Exception e1) {
                log.error("Read file error ", e1);
                return false;
            }
        }
        return true;
    }

    private void doService(CuratorFramework curator) {
        scheduledUploadData(curator, zkAddressNode);
    }

    private void scheduledUploadData(final CuratorFramework curator, final String zkAddressNode) {
        ScheduledExecutorService executorService =
                new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder()
                        .namingPattern("schedule-upload-time-%d")
                        .daemon(true)
                        .build());
        executorService
                .scheduleWithFixedDelay(() -> updateNewData(curator, zkAddressNode), 1L, 3L, TimeUnit.SECONDS);
    }

    private boolean checkInitTimeStamp(CuratorFramework curator, String zkAddressNode) throws Exception {
        byte[] bytes = curator.getData().forPath(zkAddressNode);
        Endpoint endPoint = deBuildData(new String(bytes));
        //该节点的时间不能小于最后一次上报的时间
        return endPoint.getTimestamp() <= System.currentTimeMillis();
    }

    /**
     * 创建持久顺序节点 ,并把节点数据放入 value
     *
     * @param curator
     * @return
     * @throws Exception
     */
    private String createNode(CuratorFramework curator) throws Exception {
        try {
            return curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(foreverPath + "/" + listenAddress + "-", buildData().getBytes());
        } catch (Exception e) {
            log.error("create node error msg {} ", e.getMessage());
            throw e;
        }
    }

    private void updateNewData(CuratorFramework curator, String path) {
        try {
            if (System.currentTimeMillis() < lastUpdateTime) {
                return;
            }
            curator.setData().forPath(path, buildData().getBytes());
            lastUpdateTime = System.currentTimeMillis();
        } catch (Exception e) {
            log.info("update init data error path is {} error is {}", path, e);
        }
    }

    /**
     * 构建需要上传的数据
     *
     * @return
     */
    private String buildData() {
        Endpoint endpoint = new Endpoint(ip, port, System.currentTimeMillis());
        return FastJsonUtil.toJson(endpoint);
    }

    private Endpoint deBuildData(String json) {
        return FastJsonUtil.getBean(json, Endpoint.class);
    }

    /**
     * 在节点文件系统上缓存一个workid值,zk失效,机器重启时保证能够正常启动
     *
     * @param workerID
     */
    private void updateLocalWorkerID(int workerID) {
        File leafconfFile = new File(propPath.replace("{port}", port));
        boolean exists = leafconfFile.exists();
        log.info("file exists status is {}", exists);
        if (exists) {
            try {
                FileUtils.writeStringToFile(leafconfFile, "workerID=" + workerID,charset, false);
                log.info("update file cache workerID is {}", workerID);
            } catch (IOException e) {
                log.error("update file cache error ", e);
            }
        } else {
            //不存在文件,父目录页肯定不存在
            try {
                boolean mkdirs = leafconfFile.getParentFile().mkdirs();
                log.info("init local file cache create parent dis status is {}, worker id is {}", mkdirs, workerID);
                if (mkdirs) {
                    if (leafconfFile.createNewFile()) {
                        FileUtils.writeStringToFile(leafconfFile, "workerID=" + workerID, charset, false);
                        log.info("local file cache workerID is {}", workerID);
                    }
                } else {
                    log.warn("create parent dir error===");
                }
            } catch (IOException e) {
                log.warn("craete workerID conf file error", e);
            }
        }
    }

    private CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder().connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

    /**
     * 上报数据结构
     */
    static class Endpoint implements Serializable {
        private static final long serialVersionUID = 4239727575457996039L;
        private String ip;
        private String port;
        private long timestamp;

        public Endpoint() {
        }

        public Endpoint(String ip, String port, long timestamp) {
            this.ip = ip;
            this.port = port;
            this.timestamp = timestamp;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public String getzkAddressNode() {
        return zkAddressNode;
    }

    public void setzkAddressNode(String zkAddressNode) {
        this.zkAddressNode = zkAddressNode;
    }

    public String getListenAddress() {
        return listenAddress;
    }

    public void setListenAddress(String listenAddress) {
        this.listenAddress = listenAddress;
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }
}
