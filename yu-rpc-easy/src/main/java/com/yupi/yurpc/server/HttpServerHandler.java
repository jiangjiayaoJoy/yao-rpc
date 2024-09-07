package com.yupi.yurpc.server;

/**
 * ClassName: HttpServerHandler
 * Package: com.yupi.yurpc.server
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/7 16:29
 * @Version 1.0
 */

import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.RpcResponse;
import com.yupi.yurpc.registry.LocalRegistry;
import com.yupi.yurpc.serializer.JdkSerializer;
import com.yupi.yurpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * http请求处理器(rpc核心)
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    //注意：不同的web服务器对应的请求处理器方式也不同，
    // 比如Vert.x是通过实现Handler<HttpServerRequest>接口来自定义请求处理器的。
    //并且可以通过request.bodyHandler来异步处理请求
    @Override
    public void handle(HttpServerRequest request) {
        //1.将http请求反序列化为RpcRequest对象，并从对象种获得请求参数
        //2.根据参数(服务名方法名)从本地服务注册器种获取到对应的服务实现类
        //3.通过反射调用该实现类的方法，得到返回结果
        //4.对返回结果进行封装和序列化，并写入到响应中

        //指定序列化器
        final JdkSerializer serializer = new JdkSerializer();
        //记录日志
        System.out.println("Received request:"+request.method()+" "+request.uri());
        //异步处理HTTP请求
        request.bodyHandler(body->{
            //将请求参数进行反序列化并封装为对象
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest=null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            //如果请求为null，直接返回
            if(rpcRequest==null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request,rpcResponse,serializer);
                return;
            }

            try {
                //获取需要调用的服务实体类，并通过反射调用
                Class<?> implClass=LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            //返回响应
            doResponse(request,rpcResponse,serializer);
        });
    }

    /**
     * 返回响应
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer){
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type","application/json");//内容格式：json
        //序列化
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }

}
