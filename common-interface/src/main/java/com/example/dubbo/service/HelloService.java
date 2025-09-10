package com.example.dubbo.service;

/**
 * 公共服务接口
 */
public interface HelloService {
    /**
     * 简单的问候方法
     * @param name 名称
     * @return 问候语
     */
    String sayHello(String name);
    
    /**
     * 获取服务信息
     * @return 服务信息
     */
    String getServiceInfo();
}
    