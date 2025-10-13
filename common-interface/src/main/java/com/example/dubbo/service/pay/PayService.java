package com.example.dubbo.service.pay;

public interface PayService {

    String weChatPay(String name);

    String aliPay(String name);
}
