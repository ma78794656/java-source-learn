package com.yqxzs.learn.service;

import com.yqxzs.learn.annotation.MyService;

/**
 * @author edz
 */
@MyService
public class TestServiceImpl implements TestService{
    @Override
    public void printParameter(String param) {
        System.out.println("TestService: param=" + param);
    }
}
