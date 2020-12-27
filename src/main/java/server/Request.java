package server;

import java.io.IOException;
import java.io.InputStream;

/**
 * 把我们用到的请求信息，封装成Response对象 （根据inputSteam输入流封装）
 */
public class Request {
    /**
     * 请求方式 例如：GET/POST
     */
    private String method;

    /**
     * / ， /index.html
     */
    private String url;

    /**
     * 其他的属性都是通过inputStream解析出来的。
     */
    private InputStream inputStream;

    /**
     * 构造器 输入流传入
     */
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        //从输入流中获取请求信息
        int count = 0 ;
        while (count==0){
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String inputsStr = new String(bytes);
        //获取第一行数据
        String firstLineStr = inputsStr.split("\\n")[0];  //GET / HTTP/1.1
        String[] strings = firstLineStr.split(" ");
        this.method=strings[0];
        this.url= strings[1];

        System.out.println("method=====>>"+method);
        System.out.println("url=====>>"+url);
    }

    public Request() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
