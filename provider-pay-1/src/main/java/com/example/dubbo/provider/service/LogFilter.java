//package com.example.dubbo.provider.service;
//
//import org.apache.dubbo.common.constants.CommonConstants;
//import org.apache.dubbo.common.extension.Activate;
//import org.apache.dubbo.rpc.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * 自定义Dubbo日志过滤器
// * order属性定义执行顺序（可选），数值越小越先执行
// */
//@Activate(group = {CommonConstants.CONSUMER}, order = -10000)
//public class LogFilter implements Filter {
//    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        // 获取调用的服务名和方法名
//        String serviceName = invoker.getInterface().getName();
//        String methodName = invocation.getMethodName();
//        String remoteHost = RpcContext.getContext().getRemoteHost(); // 获取远程地址
//        boolean isProviderSide = RpcContext.getContext().isProviderSide(); // 判断是否为提供方
//
//        // 记录调用开始时间
//        long startTime = System.currentTimeMillis();
//
//        // 记录请求日志
//        if (logger.isInfoEnabled()) {
//            logger.info("Dubbo {} request - service: {}, method: {}, remote: {}",
//                       isProviderSide ? "provider" : "consumer",
//                       serviceName, methodName, remoteHost);
//        }
//        System.out.println("执行前");
//        // 执行后续调用链
//        Result result = invoker.invoke(invocation);
//        // 计算调用耗时
//        long elapsed = System.currentTimeMillis() - startTime;
//        System.out.println("执行后");
//        return result;
//    }
//}