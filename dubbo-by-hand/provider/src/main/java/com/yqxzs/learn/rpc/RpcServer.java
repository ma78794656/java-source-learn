package com.yqxzs.learn.rpc;

/**
 * @author edz
 */
public interface RpcServer {
    /** 服务端server
     * @param hostName ip
     * @param port port
     */
    void start(String hostName, int port);
}
