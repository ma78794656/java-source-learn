package com.yqxzs.learn.tomcat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author edz
 */
public class ServletMappingConfig {
    public static List<ServletMapping> servletMappings = new ArrayList<>();

    static {
        servletMappings.add(new ServletMapping("index", "/index", "com.yqxzs.learn.tomcat.test.IndexServlet"));
        servletMappings.add(new ServletMapping("blog", "/blog", "com.yqxzs.learn.tomcat.test.BlogServlet"));
    }
}
