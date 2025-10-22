package com.example.dubbo.provider.service;

import com.example.dubbo.service.pay.PayService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.config.annotation.DubboService;

// 统一暴露的支付服务（group相同）
@DubboService(group = "pay-service-group")
public class PayServiceFacade implements PayService {

    private final PayService adaptivePayService = ExtensionLoader.getExtensionLoader(PayService.class).getAdaptiveExtension();

    @Override
    public String pay(String name, URL url) {
        return adaptivePayService.pay(name,url);
    }
}