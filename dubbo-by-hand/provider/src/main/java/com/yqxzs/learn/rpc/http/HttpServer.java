package com.yqxzs.learn.rpc.http;

import com.yqxzs.learn.rpc.RpcServer;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

/**
 * @author edz
 */
public class HttpServer implements RpcServer {
    public void start(String hostName, int port) {
        Tomcat tomcat = new Tomcat();
        // server
        Server server = tomcat.getServer();

        // service
        Service service = server.findService("Tomcat");

        // engine
        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostName);

        // host
        Host host = new StandardHost();
        host.setName(hostName);

        // context
        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        // connector
        Connector connector = new Connector();
        connector.setPort(port);

        //
        host.addChild(context);
        engine.addChild(host);
        service.setContainer(engine);
        service.addConnector(connector);

        // servlet
        tomcat.addServlet(contextPath,"dispather",new DispatcherServlet());
        context.addServletMappingDecoded("/*","dispather");
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
