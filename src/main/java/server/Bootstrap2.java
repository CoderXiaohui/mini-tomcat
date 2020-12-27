package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Minicat的主类
 * 2.0版本
 */
public class Bootstrap2 {
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
        Bootstrap2 bootstrap = new Bootstrap2();
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
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========>>Minicat start on port："+port);

        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            //封装Resuest对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());
            socket.close();
        }

    }

}
