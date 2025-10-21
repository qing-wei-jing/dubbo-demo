package com.example.dubbo.consumer.controller;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

// 自定义负载均衡器：随机选择可用实例
public class SimpleRandomLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "simpleRandom";

    // 固定随机实例，避免频繁创建
    private static final Random RANDOM = new Random();

    @Override
    public <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        // 1. 过滤出所有可用的服务提供者（Dubbo的Invoker有isAvailable()方法判断可用性）
        List<Invoker<T>> availableInvokers = invokers.stream()
                .filter(Invoker::isAvailable)
                .toList();

        // 2. 如果没有可用实例，返回第一个（Dubbo默认兜底逻辑）
        if (availableInvokers.isEmpty()) {
            return invokers.get(0);
        }

        // 3. 从可用实例中随机选一个
        int index = RANDOM.nextInt(availableInvokers.size());
        return availableInvokers.get(index);
    }
}