package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * Minicat的主类
 * 优化版本——使用多线程（使用线程池）
 */
public class Bootstrap5 {
    private Map<String,HttpServlet> servletMap = new HashMap<>();


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
        Bootstrap5 bootstrap = new Bootstrap5();
        try {
            //启动Minicat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MiniCat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {
        //加载解析相关的配置 ，web.xml，把配置的servlet存入servletMap中
        loadServlet();

        /**
         * 定义线程池
         */
        //基本大小
        int corePoolSize = 10;
        //最大
        int maxPoolSize = 50;
        //如果线程空闲的话，超过多久进行销毁
        long keepAliveTime = 100L;
        //上面keepAliveTime的单位
        TimeUnit unit = TimeUnit.SECONDS;
        //请求队列
        BlockingQueue<Runnable> workerQueue = new ArrayBlockingQueue<>(50);
        //线程工厂，使用默认的即可
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //拒绝策略，如果任务太多处理不过来了，如何拒绝
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize
                ,maxPoolSize
                ,keepAliveTime
                ,unit
                ,workerQueue
                ,threadFactory
                ,handler);

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========>>Minicat start on port(多线程)："+port);
        /**
         * 可以请求动态资源
         */
        while (true){
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            threadPoolExecutor.execute(requestProcessor);
        }
    }

    /**
     * 加载加息web.xml，初始化servlet
     */
    private void loadServlet(){
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            //根元素
            Element rootElement = document.getRootElement();
            /**
             * 1, 找到所有的servlet标签，找到servlet-name和servlet-class
             * 2, 根据servlet-name找到<servlet-mapping>中与其匹配的<url-pattern>
             */
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                /**
                 * 1, 找到所有的servlet标签，找到servlet-name和servlet-class
                 */
                //<servlet-name>dxh</servlet-name>
                Element servletNameElement =(Element)element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();
                //<servlet-class>server.DxhServlet</servlet-class>
                Element servletClassElement =(Element)element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();

                /**
                 * 2, 根据servlet-name找到<servlet-mapping>中与其匹配的<url-pattern>
                 */
                //Xpath表达式：从/web-app/servlet-mapping下查询，查询出servlet-name=servletName的元素
                Element servletMapping =(Element)rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']'");
                // /dxh
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern,(HttpServlet) Class.forName(servletClass).newInstance());
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
