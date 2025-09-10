package com.example.dubbo.consumer.controller;

import com.example.dubbo.consumer.service.HelloServiceConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @Autowired
    private HelloServiceConsumer helloServiceConsumer;
    
    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        return helloServiceConsumer.sayHello(name);
    }
    
    @GetMapping("/provider-info")
    public String getProviderInfo() {
        return "当前调用的服务: " + helloServiceConsumer.getProviderInfo();
    }
}
    