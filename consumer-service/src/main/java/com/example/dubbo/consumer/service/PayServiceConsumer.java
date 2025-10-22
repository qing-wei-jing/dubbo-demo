package com.example.dubbo.consumer.service;

import com.example.dubbo.service.hello.HelloService;
import com.example.dubbo.service.pay.PayService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

@Service
public class PayServiceConsumer {
    // 引用远程服务，group与服务提供者一致
    @DubboReference(group = "pay-service-group")
    private PayService payService;

    public String weChatPay(String name) {
        RpcContext.getClientAttachment().setAttachment("payType", "wechat");
        URL url = RpcContext.getServiceContext().getUrl();
        return payService.pay("wechat " + name, url);
    }

    public String zhifubaoPay(String name) {
        RpcContext.getClientAttachment().setAttachment("payType", "zhifubao");
        URL url = RpcContext.getServiceContext().getUrl();
        return payService.pay("zhifubao " + name, url);
    }
}
