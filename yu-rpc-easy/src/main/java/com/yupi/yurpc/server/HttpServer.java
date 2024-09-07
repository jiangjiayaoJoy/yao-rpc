package com.yupi.yurpc.server;

/**
 * ClassName: HttpServer
 * Package: com.yupi.yurpc.yurpc.server
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/6 15:23
 * @Version 1.0
 */

/**
 * HTTP服务器接口
 */
public interface HttpServer {
    /**
     * 启动服务
     * @param port
     */
    void doStart(int port);
}
