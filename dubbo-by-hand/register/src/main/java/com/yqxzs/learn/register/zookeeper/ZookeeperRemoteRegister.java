package com.yqxzs.learn.register.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.yqxzs.learn.api.entity.URL;
import com.yqxzs.learn.register.RemoteRegister;

import java.util.List;
import java.util.Random;

/**
 * @author edz
 */
public class ZookeeperRemoteRegister implements RemoteRegister {

    private ZookeeperClient client;

    public ZookeeperRemoteRegister(ZookeeperClient zookeeperClient) {
        this.client = zookeeperClient;
    }

    public void register(String interfaceName, URL host) {
        try{
            StringBuilder nodePath = new StringBuilder("/");
            nodePath.append(interfaceName).append("/").append(JSONObject.toJSONString(host));
            if (client.started()){
                client.createNodePath(nodePath.toString(),"111");
            }
        }catch (Exception e){

        }
    }

    public URL getRandomURL(String interfaceName) {
        try {
            StringBuilder nodepath = new StringBuilder("/");
            nodepath.append(interfaceName);
            List<URL> urls = client.getChildNodes(nodepath.toString());
            Random random = new Random();
            int i = random.nextInt(urls.size());
            return urls.get(i);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
