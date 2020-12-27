package server;

import java.io.IOException;

/**
 * @Author: Dengxh
 * @Date: 2020/12/27 21:21
 * @Description:
 */
public class DxhServlet extends HttpServlet{
    @Override
    public void doGet(Request request, Response response) {
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        String content="<h1>DxhServlet get</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content="<h1>DxhServlet post</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
