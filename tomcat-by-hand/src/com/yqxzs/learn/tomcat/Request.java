package com.yqxzs.learn.tomcat;
import	java.io.IOException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author edz
 */
public class Request {
    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    public Request(InputStream inputStream) throws IOException {
        String httpRequest = "";
        byte[] httpRequestBytes = new byte[1024];
        int length = inputStream.read(httpRequestBytes);

        if (length < 0) {
            throw new IOException("no content in input stream");
        }
        httpRequest = new String(httpRequestBytes);

        // header
        String httpHeader = httpRequest.split("\n")[0];
        System.out.println("Http Header: " + httpHeader);

        // url
        method = httpHeader.split("\\s")[0];
        System.out.println("Http Method" + method);

        // method
        url = httpHeader.split("\\s")[1];
        System.out.println("Http Url" + url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
