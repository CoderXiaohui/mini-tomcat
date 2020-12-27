package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Minicat的主类
 * 1.0版本
 */
public class Bootstrap {
    /**
     * 定义Socket监听的端口号
     */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Minicat的启动入口
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            //启动Minicat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * MiniCat启动需要初始化展开的一些操作
     */
    public void start() throws IOException {
        /*
           完成Minicat 1.0版本
           需求：浏览器请求http://localhost:8080，返回一个固定的字符串到页面“Hello Minicat！”）
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========>>Minicat start on port："+port);

        while(true){
            Socket socket = serverSocket.accept();
            //有了socket，接收到请求,获取输出流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello Minicat!";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length)+data;
            outputStream.write(responseText.getBytes());
            socket.close();
        }
    }

}
