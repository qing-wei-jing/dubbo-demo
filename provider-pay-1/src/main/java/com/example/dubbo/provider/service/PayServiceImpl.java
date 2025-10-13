package com.example.dubbo.provider.service;

import com.example.dubbo.service.pay.PayService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(group = "pay-service-group")
public class PayServiceImpl implements PayService {

    @Override
    public String weChatPay(String name) {
        return "provider1-wechatPay" + name;
    }

    @Override
    public String aliPay(String name) {
        return "provider1-aliPay" + name;
    }
}
