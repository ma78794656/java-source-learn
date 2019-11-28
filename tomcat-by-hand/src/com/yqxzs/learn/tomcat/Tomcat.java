package com.yqxzs.learn.tomcat;
import	java.io.OutputStream;
import	java.net.Socket;
import	java.io.InputStream;
import java.io.IOException;
import java.net.ServerSocket;
import	java.util.HashMap;
import	java.util.Map;

/**
 * @author edz
 */
public class Tomcat {
    /**
     * 端口号
     */
    private int port = 8081;
    /**
     * url -> 对应的处理servlet
     */
    private Map<String, String> urlServletMapping = new HashMap<String, String> ();

    public Tomcat(Integer port) {
        this.port = port;
    }

    public void start() {
        initServletMapping();

        while (true) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("server start at port: " + port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();

                    Request request = new Request(inputStream);
                    Response response = new Response(outputStream);

                    dispatch(request, response);

                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void initServletMapping() {
        for (ServletMapping servletMapping: ServletMappingConfig.servletMappings) {
            urlServletMapping.put(servletMapping.getUrl(), servletMapping.getClazz());
        }
    }

    private void dispatch(Request req, Response resp) {
        String clazz = urlServletMapping.get(req.getUrl());
        if (clazz == null) {
            return;
        }

        try {
            Class<Servlet> servletClass = (Class<Servlet>) Class.forName(clazz);
            Servlet servlet = servletClass.newInstance();
            servlet.service(req, resp);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat(8081);
        tomcat.start();
    }
}
