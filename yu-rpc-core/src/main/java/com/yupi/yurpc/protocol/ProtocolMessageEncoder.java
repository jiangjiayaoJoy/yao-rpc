package com.yupi.yurpc.protocol;

import com.yupi.yurpc.serializer.Serializer;
import com.yupi.yurpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * ClassName: ProtocolMessageEncoder
 * Package: com.yupi.yurpc.protocol
 * Description:tcp编码器
 * 将自定义协议消息类转化为字节数组写入到tcp连接的buffer中去
 *
 * @Author Joy_瑶
 * @Create 2024/6/19 17:18
 * @Version 1.0
 */
public class ProtocolMessageEncoder {
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if(protocolMessage==null||protocolMessage.getHeader()==null){
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();
        //将请求头依次写入buffer中
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());
//        buffer.appendInt(header.getBodyLength());
        //需要将请求体写入到buffer中，这种将java类转换为字节的工作需要序列化器来做，所以这里需要获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum==null){
            throw new RuntimeException("序列化协议不存在！");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());
        //写入请求体body
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;
    }
}
