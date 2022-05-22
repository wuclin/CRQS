package com;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperUtil {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final String ZNODE = "/";

    private static ZkClient client;

    static {
        client =  new ZkClient(ZK_ADDRESS);
    }

    public static void writeData(String path, String data) {
        if (!client.exists(ZNODE)){
            //创建持久化节点 ,初始化数据
            String[] paths = path.split("/");
            client.createPersistent(ZNODE, "/" + paths[1]);
            client.createPersistent(ZNODE + "/" + paths[1], "/" + paths[2]);
            String chlid = client.create(ZNODE + path, data, CreateMode.PERSISTENT);
            System.out.println(chlid);
        }else {
            //修改节点数据,并返回该节点的状态
            String[] paths = path.split("/");
            client.createPersistent(ZNODE + path);
            Stat znodeStat = client.writeDataReturnStat(ZNODE + path, data, -1);
            System.out.println(znodeStat);
        }
    }

    public static <T> T readData(String path) {
        //获取节点数据
        return client.readData(ZNODE + path);
    }
}