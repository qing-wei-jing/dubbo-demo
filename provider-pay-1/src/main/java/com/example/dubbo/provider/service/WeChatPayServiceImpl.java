package com.example.dubbo.provider.service;

import com.example.dubbo.service.pay.PayService;
import org.apache.dubbo.common.Extension;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;

//@DubboService(group = "wechat-pay-service-group")
public class WeChatPayServiceImpl implements PayService {
    @Override
    public String pay(String name, URL url) {
        return "provider1-wechatPay: " + name;
    }
}
