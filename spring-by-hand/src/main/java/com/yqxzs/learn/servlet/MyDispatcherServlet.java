package com.yqxzs.learn.servlet;
import	java.lang.reflect.Constructor;

import com.yqxzs.learn.annotation.MyAutowired;
import com.yqxzs.learn.annotation.MyController;
import com.yqxzs.learn.annotation.MyRequestMapping;
import com.yqxzs.learn.annotation.MyService;

import	java.io.File;
import java.lang.reflect.Field;
import	java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import	java.lang.reflect.Method;
import java.util.*;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author edz
 */
public class MyDispatcherServlet extends HttpServlet {
    private Logger log = Logger.getLogger("init");
    private Properties properties = new Properties();
    private List<String> classNames = new ArrayList<>();
    private Map<String, Object> ioc = new HashMap<> ();
    private Map < String, Method> handlerMapping = new HashMap<> ();
    private Map<String, Object> controllerMapping = new HashMap<> ();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        log.info("初始化MyDispatcherServlet...");

        log.info("contextConfigLocation = " + config.getInitParameter("contextConfigLocation"));
        // 加载配置文件，填充properties字段
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 根据properties，扫描用户指定的包下的所有类
        doScanner(properties.getProperty("scanPackage"));

        // 实例化指定注解的类
        doInstance();

        // 依赖注入
        doAutowired();

        // handler mapping初始化
        initHandlerMapping();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //处理请求
            doDispatch(req,resp);
        } catch (Exception e) {
            resp.getWriter().write("500!! Server Exception");
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handlerMapping.isEmpty()) {
            return;
        }

        String url = req.getRequestURI();
        log.info("req.url=" + url);
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        log.info("req.url=" + url);
        for (Map.Entry<String, Method> entry : handlerMapping.entrySet()) {
            log.info("url:" + entry.getKey() + ", method" + entry.getValue().getName());
        }
        if (!handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 NOT FOUND");
            return;
        }

        Method method = handlerMapping.get(url);

        Class<?>[] paramTypes = method.getParameterTypes();

        Map<String, String[]> parameterMap = req.getParameterMap();

        Object[] paramValues = new Object[paramTypes.length];

        for (int i = 0; i<paramTypes.length; i++){
            //根据参数名称，做某些处理
            String requestParam = paramTypes[i].getSimpleName();
            if (requestParam.equals("HttpServletRequest")){
                //参数类型已明确，这边强转类型
                paramValues[i]=req;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")){
                paramValues[i]=resp;
                continue;
            }
            if(requestParam.equals("String")){
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    paramValues[i]=value;
                }
            }
        }
        //利用反射机制来调用
        try {
            //obj是method所对应的实例 在ioc容器中
            method.invoke(this.controllerMapping.get(url), paramValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doLoadConfig(String location) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(location);
        if (resourceAsStream == null) {
            throw new RuntimeException("读取"+location+"中的文件失败");
        }

        try {
            log.info("读取"+location+"中的文件...");
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        assert url != null;
        File dir = new File(url.getFile());
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                doScanner(packageName + "." + f.getName());
            } else {
                String className = packageName + "." + f.getName().replace(".class", "");
                log.info("add class: " + className);
                classNames.add(className);
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyController.class)) {
                    ioc.put(toLowerFirstWord(clazz.getSimpleName()), clazz.newInstance());
                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    MyService service = clazz.getAnnotation(MyService.class);
                    String beanName = service.value();
                    if ("".equals(beanName.trim())) {
                        beanName = toLowerFirstWord(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);

                    Class[] interfaces = clazz.getInterfaces();
                    for (Class intf : interfaces) {
                        ioc.put(intf.getName(), instance);
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(MyAutowired.class)) {
                    continue;
                }

                MyAutowired autowired = field.getAnnotation(MyAutowired.class);
                String beanName = autowired.value();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz =  entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }

            String baseUrl = "";
            if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if(!method.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }

                MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
                String url = requestMapping.value();
                url = (baseUrl + "/" + url).replaceAll("/+", "/");
                handlerMapping.put(url, method);

                Object controllerInstance = ioc.get(toLowerFirstWord(clazz.getSimpleName()));
                assert controllerInstance != null;

                controllerMapping.put(url, controllerInstance);
            }
        }
    }



    private String toLowerFirstWord(String name) {
        char [] nameBytes = name.toCharArray();
        if (nameBytes[0] <= 'a') {
            nameBytes[0] += 32;
        }
        return new String(nameBytes);
    }

}
