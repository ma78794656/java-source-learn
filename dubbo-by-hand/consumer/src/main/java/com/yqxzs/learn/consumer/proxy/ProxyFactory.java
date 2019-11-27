package com.yqxzs.learn.consumer.proxy;

import com.yqxzs.learn.api.entity.URL;
import com.yqxzs.learn.register.RegisterType;
import com.yqxzs.learn.register.RemoteRegister;
import com.yqxzs.learn.register.factory.RemoteRegisterFactory;
import com.yqxzs.learn.rpc.http.Invocation;
import com.yqxzs.learn.rpc.protocol.Protocol;
import com.yqxzs.learn.rpc.protocol.ProtocolFactory;
import com.yqxzs.learn.rpc.protocol.ProtocolType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author edz
 */
public class ProxyFactory {
    public static <T> T getProxy(final ProtocolType protocolType ,final RegisterType registerType, final Class interfaceClass){
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new InvocationHandler() {
                    public Object invoke(
                            Object proxy,
                            Method method,
                            Object[] args) throws Throwable {
                        Protocol protocl = ProtocolFactory.getProtocol(protocolType);
                        Invocation invocation = new Invocation(interfaceClass.getName(),method.getName(),method.getParameterTypes(),args);
                        RemoteRegister remoteRegister = RemoteRegisterFactory.getRemoteRegister();
                        URL radomURL = remoteRegister.getRandomURL(interfaceClass.getName());
                        System.out.println("调用地址host:"+ radomURL.getHost()+ ",port:"+radomURL.getPort());
                        return protocl.invokeProtocol(radomURL,invocation);
                    }
                });
    }
}
