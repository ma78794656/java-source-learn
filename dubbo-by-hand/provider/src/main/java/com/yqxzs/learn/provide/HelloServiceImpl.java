package com.yqxzs.learn.provide;

import com.yqxzs.learn.api.HelloService;

/**
 * @author edz
 */
public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        System.out.println("hello " + name + "from provider!");
        return "hello " + name + " from provider";
    }
}
