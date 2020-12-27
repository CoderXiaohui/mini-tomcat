package server;

/**
 * @Author: Dengxh
 * @Date: 2020/12/27 21:16
 * @Description:
 */
public abstract class HttpServlet implements Servlet{

    public abstract void doGet(Request request,Response response);
    public abstract void doPost(Request request,Response response);


    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equals(request.getMethod())){
            doGet(request, response);
        }else{
            doPost(request, response);
        }
    }
}
