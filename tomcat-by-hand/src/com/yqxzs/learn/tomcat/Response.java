package com.yqxzs.learn.tomcat;
import java.io.IOException;
import	java.io.OutputStream;

/**
 * @author edz
 */
public class Response {
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String content) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\n" +
                "Content-Type:text/html\n" +
                "\r\n" +
                "<html><head><link rel=\"icon\" href=\"data:;base64,=\"></head><body>" +
                content +
                "</body></html>";
        outputStream.write(httpResponse.getBytes());
        outputStream.close();
    }
}
