package com.yqxzs.learn.rpc.protocol;

/**
 * @author edz
 */

public enum ProtocolType {
    /**
     * rpc协议
     */
    HTTP("http", 1),
    NETTY("netty", 2);

    private String protocolName;
    private int protocolNum;

    ProtocolType(String protocolName, int protocolNum) {
        this.protocolName =protocolName;
        this.protocolNum = protocolNum;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public int getProtocolNum() {
        return protocolNum;
    }

    public void setProtocolNum(int protocolNum) {
        this.protocolNum = protocolNum;
    }
}
