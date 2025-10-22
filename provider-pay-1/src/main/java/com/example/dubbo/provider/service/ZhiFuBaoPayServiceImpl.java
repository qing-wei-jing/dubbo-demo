package com.example.dubbo.provider.service;

import com.example.dubbo.service.pay.PayService;
import org.apache.dubbo.common.Extension;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;

public class ZhiFuBaoPayServiceImpl implements PayService {
    @Override
    public String pay(String name, URL url) {
        return "provider1-zhiFuBaoPay: " + name;
    }
}