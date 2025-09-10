package com.example.dubbo.provider.service;

import com.example.dubbo.service.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(group = "hello-service-group")
public class HelloServiceImpl implements HelloService {
    
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "! This is response from Provider Service 3";
    }
    
    @Override
    public String getServiceInfo() {
        return "Provider Service 3 (端口: 8083, Dubbo端口: 20883)";
    }
}
    