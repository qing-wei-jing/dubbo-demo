package com.example.dubbo.consumer.service;

import com.example.dubbo.service.hello.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceConsumer {
    
    // 引用远程服务，group与服务提供者一致
    @DubboReference(group = "hello-service-group", loadbalance = "roundrobin")
    private HelloService helloService;
    
    public String sayHello(String name) {
        return helloService.sayHello(name);
    }

    public String sayGoodBye(String name) {
        return helloService.sayGoodBye(name);
    }
    
    public String getProviderInfo() {
        return helloService.getServiceInfo();
    }
}
    