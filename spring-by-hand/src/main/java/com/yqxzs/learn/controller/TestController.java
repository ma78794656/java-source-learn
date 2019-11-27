package com.yqxzs.learn.controller;

import com.yqxzs.learn.annotation.MyAutowired;
import com.yqxzs.learn.annotation.MyController;
import com.yqxzs.learn.annotation.MyRequestMapping;
import com.yqxzs.learn.annotation.MyRequestParam;
import com.yqxzs.learn.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author edz
 */
@MyController
@MyRequestMapping("/test")
public class TestController {
    @MyAutowired
    private TestService testService;

    @MyRequestMapping("/doTest1")
    public void test1(
            HttpServletRequest req,
            HttpServletResponse resp,
            @MyRequestParam("param") String param) {
        try {
            resp.getWriter().write("/doTest1 success, param = " + param);
            testService.printParameter(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
