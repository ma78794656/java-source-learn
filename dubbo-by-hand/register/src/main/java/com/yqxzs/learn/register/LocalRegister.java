package com.yqxzs.learn.register;

/**
 * @author edz
 */
public interface LocalRegister {
    /** 注册
     * @param interfaceName 接口名
     * @param interfaceImplClass 接口实现类
     */
    void register(String interfaceName, Class interfaceImplClass);

    /** 获取接口实例
     * @param interfaceName 接口名
     * @return 实现类
     */
    Class get(String interfaceName);
}
