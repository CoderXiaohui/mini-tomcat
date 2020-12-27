package server;

/**
 * @Author: Dengxh
 * @Date: 2020/12/27 21:14
 * @Description:
 */
public interface Servlet {
    void init() throws Exception;
    void destroy() throws Exception;
    void service(Request request,Response response) throws Exception;
}
