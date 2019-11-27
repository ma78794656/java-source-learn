package com.yqxzs.learn.rpc.http;

import com.yqxzs.learn.api.entity.URL;
import com.yqxzs.learn.rpc.protocol.Protocol;

/**
 * @author edz
 */
public class HttpProtocol implements Protocol {
    public Object invokeProtocol(URL url, Invocation invocation) {
        HttpClient httpClient = new HttpClient();
        return httpClient.post(url.getHost(),url.getPort(),invocation);
    }

    public void start(URL url) {
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHost(),url.getPort());
    }
}
