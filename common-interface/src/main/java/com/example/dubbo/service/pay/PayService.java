package com.example.dubbo.service.pay;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI("wechat")
public interface PayService {

    @Adaptive({"payType"})
    String pay(String name, URL url);

}
