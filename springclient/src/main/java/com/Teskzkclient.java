package com;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class Teskzkclient {
    public static void main(String[] args){

        ZkClient zkClient = new ZkClient("127.0.0.1:2181");

        List<String> list = zkClient.getChildren("/");

        for (String i : list)
            System.out.println(i);


    }
}
