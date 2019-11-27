package com.yqxzs.learn;

import com.yqxzs.learn.api.HelloService;
import com.yqxzs.learn.api.entity.URL;
import com.yqxzs.learn.provide.HelloServiceImpl;
import com.yqxzs.learn.register.LocalRegister;
import com.yqxzs.learn.register.RegisterType;
import com.yqxzs.learn.register.RemoteRegister;
import com.yqxzs.learn.register.factory.LocalRegisterFactory;
import com.yqxzs.learn.register.factory.RemoteRegisterFactory;
import com.yqxzs.learn.rpc.protocol.Protocol;
import com.yqxzs.learn.rpc.protocol.ProtocolFactory;
import com.yqxzs.learn.rpc.protocol.ProtocolType;

/**
 * @author edz
 */
public class Provider {
    public static void main(String[] args) {
        URL url = new URL("localhost",8022);
        //远程服务注册地址
        RemoteRegister register = RemoteRegisterFactory.getRemoteRegister();
        register.register(HelloService.class.getName(),url);

        //本地注册服务的实现类
        LocalRegister localRegister = LocalRegisterFactory.getLocalMapRegister();
        localRegister.register(HelloService.class.getName(), HelloServiceImpl.class);

        Protocol protocol = ProtocolFactory.getProtocol(ProtocolType.HTTP);
        protocol.start(url);
    }
}
