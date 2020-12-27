package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 *
 */
public class RequestProcessor extends Thread{
    private  Socket socket;
    private Map<String,HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();
            //封装Resuest对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            String url = request.getUrl();
            //静态资源处理
            if (servletMap.get(url)==null){
                response.outputHtml(request.getUrl());
            }else{
                //动态资源处理
                HttpServlet httpServlet = servletMap.get(url);
                httpServlet.service(request,response);
            }
            socket.close();
        }catch (Exception e){

        }
    }
}
