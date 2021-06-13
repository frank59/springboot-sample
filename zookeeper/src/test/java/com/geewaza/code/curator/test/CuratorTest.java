package com.geewaza.code.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-06-13 17:21
 **/
public class CuratorTest {

    private CuratorFramework client;

    @Before
    public void before() {
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.10.10:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(new ExponentialBackoffRetry(3000, 10))
                .build();
        client.start();
    }

    @After
    public void close() {
        client.close();
    }


    @Test
    public void testConnect() {
//        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.10:2181"
//                ,60* 1000
//                , 15 * 1000
//                , new ExponentialBackoffRetry(3000, 10));
//        client.start();

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.10.10:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(new ExponentialBackoffRetry(3000, 10))
                .namespace("geewaza-test")
                .build();
        client.start();
    }


    @Test
    public void testCreate() throws Exception {
        // 1 基础创建 默认类型：持久化
        String path = client.create().forPath("/app1", "hehe".getBytes());
        System.out.println(path);

        // 创建临时节点
        path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/app3", "hehe".getBytes());
        System.out.println(path);

        // 创建多级节点
        path = client.create().creatingParentsIfNeeded().forPath("/app4/p1");
        System.out.println(path);
    }

    @Test
    public void testGet() throws Exception {
        Stat stat = new Stat();
        System.out.println(stat);
        byte[] data = client.getData().storingStatIn(stat).forPath("/app1");
        System.out.println(new String(data));
        System.out.println(stat);
    }

}
