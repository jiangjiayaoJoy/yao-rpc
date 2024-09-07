package com.yupi.yurpc.server;

import io.vertx.core.Vertx;

/**
 * ClassName: VertxHttpServer
 * Package: com.yupi.yurpc.yurpc.server
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/6 15:26
 * @Version 1.0
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //监听端口并处理请求
        server.requestHandler(request->{
            //处理HTTP请求
            System.out.println("Received request:"+request.method()+" "+request.uri());

            //发送HTTP响应
            request.response()
                    .putHeader("content-type","text/plain")
                    .end("Hello from Vert.x server!");
        });
        server.requestHandler(new HttpServerHandler());

        //启动HTTP服务器并监听指定端口
        server.listen(port,result->{
            if(result.succeeded()){
                System.out.println("Server is now listening on port "+port);
            }else{
                System.out.println("Failed to start server: "+result.cause());
            }
        });
    }
}
