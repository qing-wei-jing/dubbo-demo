package com.example.dubbo.provider.service;

import com.example.dubbo.service.hello.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * HelloService接口的实现类
 */
@DubboService(group = "hello-service-group")
public class HelloServiceImpl implements HelloService {
    
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "! This is response from Provider Service 1";
    }

    @Override
    public String sayGoodBye(String name) {
        return "GoodBye, " + name + "! This is response from Provider Service 1";
    }

    @Override
    public String getServiceInfo() {
        return "Provider Service 1 (端口: 8081, Dubbo端口: 20881)";
    }
}
    