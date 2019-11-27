package com.yqxzs.learn.rpc.http;
import java.lang.reflect.InvocationTargetException;
import	java.lang.reflect.Method;
import com.yqxzs.learn.register.factory.LocalRegisterFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import	java.io.InputStream;
import java.io.ObjectInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author edz
 */
public class HttpHandler {
    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // 从req流里获取数据
            InputStream is = req.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(is);

            // 从流中读取数据，反序列化称实体类
            Invocation invocation = (Invocation)objectInputStream.readObject();

            // 获取服务名称
            String interfaceName = invocation.getInterfaceName();

            // server从本地注册信息中获取接口的实现类
            Class<?> interfaceImplClass = LocalRegisterFactory.getLocalMapRegister().get(interfaceName);

            // 获取类型方法
            Method method = interfaceImplClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());

            // 通过反射，调用方法 (可以优化，不用每次new instance)
            String result = (String)method.invoke(interfaceImplClass.newInstance(), invocation.getParams());

            // 返回结果
            IOUtils.write(result, resp.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
