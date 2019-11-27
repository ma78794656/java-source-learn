package com.yqxzs.learn.register;

import com.yqxzs.learn.api.entity.URL;

/**
 * @author edz
 */
public interface RemoteRegister {
    /** 注册
     * @param interfaceName 服务名
     * @param host url
     */
    void register(String interfaceName, URL host);

    /** 随机获取一个注册地址
     * @param interfaceName 服务名
     * @return url
     */
    URL getRandomURL(String interfaceName);
}
