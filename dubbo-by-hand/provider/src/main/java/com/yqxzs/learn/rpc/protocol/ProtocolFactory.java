package com.yqxzs.learn.rpc.protocol;

import com.yqxzs.learn.rpc.http.HttpProtocol;

/**
 * @author edz
 */
public class ProtocolFactory {
    private static HttpProtocol httpProtocol = new HttpProtocol();
    public static Protocol getProtocol(ProtocolType protoclType){
        if (protoclType == ProtocolType.HTTP) {
            return httpProtocol;
        }
        return null;
    }
}
