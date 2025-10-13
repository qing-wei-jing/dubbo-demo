package com.example.dubbo.consumer.service;

import com.example.dubbo.service.hello.HelloService;
import com.example.dubbo.service.pay.PayService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class PayServiceConsumer {
    // 引用远程服务，group与服务提供者一致

    private PayService payService;

    public String weChatPay(String name) {
        return payService.weChatPay(name);
    }

    public String aliPay(String name) {
        return payService.aliPay(name);
    }

}
