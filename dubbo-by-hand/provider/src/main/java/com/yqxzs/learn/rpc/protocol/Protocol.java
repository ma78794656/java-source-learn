package com.yqxzs.learn.rpc.protocol;

import com.yqxzs.learn.api.entity.URL;
import com.yqxzs.learn.rpc.http.Invocation;

/**
 * @author edz
 */
public interface Protocol {
    /**
     * 远程调用
     * @param url url
     * @param invocation in
     * @return obj
     */
    Object invokeProtocol(URL url, Invocation invocation);

    /**
     * 服务开启
     * @param url a
     */
    void start(URL url);
}
