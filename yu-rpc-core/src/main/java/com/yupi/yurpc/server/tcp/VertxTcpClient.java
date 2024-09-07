package com.yupi.yurpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.RpcResponse;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: VertxTcpClient
 * Package: com.yupi.yurpc.server.tcp
 * Description:TCP客户端
 * 先创建vertx客户端实例，然后建立连接并发送请求
 *
 * @Author Joy_瑶
 * @Create 2024/6/19 16:39
 * @Version 1.0
 */
public class VertxTcpClient {
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws Exception {
        //发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(),result->{
            if(!result.succeeded()){
                System.out.println("Failed to connect to TCP server!");
                return;
            }

            System.out.println("Connected to TCP server");
            NetSocket socket = result.result();
            //发送数据
            //构造消息
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            //构造请求头
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            //生成全局请求ID
            header.setRequestId(IdUtil.getSnowflakeNextId());
            //构造完整消息
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);

            //编码请求
            try {
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encodeBuffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }

            //接受响应
            TcpBufferHandlerWrapper bufferHandlerWrapper=new TcpBufferHandlerWrapper(buffer->{
                try {
                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>)ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                } catch (IOException e) {
                    throw new RuntimeException("协议消息解码错误");
                }
            });
            socket.handler(bufferHandlerWrapper);
        });

//        //阻塞，直到响应完成，才会继续向下执行
//        RpcResponse rpcResponse = responseFuture.get();
//        //记得关闭连接
//        netClient.close();
//        return rpcResponse;
        RpcResponse rpcResponse=null;
        rpcResponse=responseFuture.get(50, TimeUnit.SECONDS);
        netClient.close();
        return rpcResponse;
    }
    public void start(){
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //建立连接，并发送请求
        vertx.createNetClient().connect(8888,"localhost",result->{
            if(result.succeeded()){
                System.out.println("Connected to TCP server");
                NetSocket socket = result.result();
//                for(int i=0;i<1000;i++){
//                    //发送数据
//                    Buffer buffer = Buffer.buffer();
//                    String str="Hello, server!Hello, server!Hello, server!Hello, server!";
//                    buffer.appendInt(0);
//                    buffer.appendInt(str.getBytes().length);
//                    buffer.appendBytes(str.getBytes());
//                    socket.write(buffer);
//                }
                socket.write("Hello, server!");
                //接受响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: "+buffer.toString());
                });
            }else{
                System.out.println("Failed to connect to TCP server!");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}

