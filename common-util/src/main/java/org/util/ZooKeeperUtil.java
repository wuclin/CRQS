package org.util;

import org.I0Itec.zkclient.ZkClient;

public class ZooKeeperUtil {
    //private static final Logger logger = LoggerFactory.getLogger(ZooKeeperUtil.class);
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static ZkClient client;

    static {
        client = new ZkClient(ZK_ADDRESS);
    }

    public static void writeData(String path, String data) {
        if (!client.exists(path)) {
            // 创建持久化节点 ,初始化数据
            client.createPersistent(path, true);
        }
        // 修改节点数据,并返回该节点的状态
        client.writeData(path, data, -1);

    }

    public static <T> T readData(String path) {
        // 获取节点数据
        return client.readData(path);
    }

}

