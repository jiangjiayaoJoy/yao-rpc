package com.yupi.yurpc.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.RpcResponse;

import java.io.IOException;

/**
 * ClassName: JsonSerializer
 * Package: com.yupi.yurpc.serializer
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/8 20:28
 * @Version 1.0
 */

/**
 * Json序列化器
 */
public class JsonSerializer implements Serializer{
    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> classType) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, classType);
        if(obj instanceof RpcRequest){
            return handleRequest((RpcRequest)obj,classType);
        }
        if(obj instanceof RpcResponse){
            return handleResponse((RpcResponse)obj,classType);
        }
        return obj;
    }

    /**
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap无法转换为原始对象，因此做了特殊处理
     * @param rpcRequest
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleRequest(RpcRequest rpcRequest,Class<T> type) throws IOException{
        Class<?>[] paramterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        //循环处理每个参数的类型
        for(int i=0;i<paramterTypes.length;i++){
            Class<?> clazz = paramterTypes[i];
            //如果类型不同，则重新处理一下类型
            if(!clazz.isAssignableFrom(args[i].getClass())){
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i]=OBJECT_MAPPER.readValue(argBytes,clazz);
            }
        }
        return type.cast(rpcRequest);
    }

    /**
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap无法转换为原始对象，因此做了特殊处理
     * @param rpcResponse
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleResponse(RpcResponse rpcResponse,Class<T> type) throws IOException{
        //处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes,rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }
}
