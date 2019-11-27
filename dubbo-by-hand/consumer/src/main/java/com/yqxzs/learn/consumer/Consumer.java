package com.yqxzs.learn.consumer;

import com.yqxzs.learn.api.HelloService;
import com.yqxzs.learn.consumer.proxy.ProxyFactory;
import com.yqxzs.learn.register.RegisterType;
import com.yqxzs.learn.rpc.protocol.ProtocolType;

/**
 * @author edz
 */
public class Consumer {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(ProtocolType.HTTP, RegisterType.ZOOKEEPER,HelloService.class);
        String result = helloService.sayHello("liuy");
        System.out.println(result);
    }
}
