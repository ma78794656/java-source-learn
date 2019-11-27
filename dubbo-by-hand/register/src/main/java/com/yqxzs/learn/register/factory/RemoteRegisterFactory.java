package com.yqxzs.learn.register.factory;

import com.yqxzs.learn.register.RemoteRegister;
import com.yqxzs.learn.register.zookeeper.ZookeeperClient;
import com.yqxzs.learn.register.zookeeper.ZookeeperRemoteRegister;

/**
 * @author edz
 */
public class RemoteRegisterFactory  {
    private static ZookeeperRemoteRegister zookeepRemoteRegister = new ZookeeperRemoteRegister(new ZookeeperClient());
    public static RemoteRegister getRemoteRegister(){
            return zookeepRemoteRegister;
    }
}
