package com.example.dubbo.consumer.controller;

import com.example.dubbo.consumer.service.HelloServiceConsumer;
import com.example.dubbo.consumer.service.PayServiceConsumer;
import com.example.dubbo.service.pay.PayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController

public class HelloController {
    
    @Resource
    private HelloServiceConsumer helloServiceConsumer;

    @Resource
    private PayServiceConsumer payServiceConsumer;
    
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name") String name, HttpServletRequest request) {
        String header = request.getHeader("content-type");
        String header2 = request.getHeader("Content-Type");
        return helloServiceConsumer.sayHello(name);
    }
    
    @GetMapping("/pay")
    public String getPay() {
        return "当前调用的服务: " + payServiceConsumer.weChatPay("张三");
    }
}
    