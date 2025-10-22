package com.example.dubbo.provider.service;

import com.example.dubbo.service.pay.PayService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(group = "zhiFuBao-pay-service-group")
public class ZhiFuBaoPayServiceImpl implements PayService {

    @Override
    public String pay(String name) {
        return "provider1-zhiFuBaoPay" + name;
    }
}
