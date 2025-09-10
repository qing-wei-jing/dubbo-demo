package com.example.dubbo.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ProviderApplication3 {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication3.class, args);
        System.out.println("Provider Service 3 started successfully!");
    }
}
    