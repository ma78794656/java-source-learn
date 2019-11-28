package com.yqxzs.learn.tomcat.test;

import com.yqxzs.learn.tomcat.Request;
import com.yqxzs.learn.tomcat.Response;
import com.yqxzs.learn.tomcat.Servlet;

import java.io.IOException;

/**
 * @author edz
 */
public class IndexServlet extends Servlet {
    @Override
    public void doPost(Request req, Response resp) {
        try {
            resp.write("url: " + req.getUrl() + ", method: " + req.getMethod() + ", content: Hello from index!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(Request req, Response resp) {
        try {
            resp.write("url: " + req.getUrl() + ", method: " + req.getMethod() + ", content: Hello from index!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
