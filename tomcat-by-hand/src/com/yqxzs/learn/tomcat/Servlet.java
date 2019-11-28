package com.yqxzs.learn.tomcat;

/**
 * @author edz
 */
public abstract class Servlet {
    public void service(Request req, Response resp) {
        if (req.getMethod().equalsIgnoreCase("POST")) {
            doPost(req, resp);
        } else if (req.getMethod().equalsIgnoreCase("GET")) {
            doGet(req, resp);
        }
    }

    public void doPost(Request req, Response resp) {

    }

    public void doGet(Request req, Response resp) {

    }
}
