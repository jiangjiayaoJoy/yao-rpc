package com.yupi.yurpc.server.tcp;

/**
 * ClassName: TcpBufferHandlerWrapper
 * Package: com.yupi.yurpc.server.tcp
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/20 21:47
 * @Version 1.0
 */

import com.yupi.yurpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;


/**
 * 装饰器模式（使用recordParser的读取固定长度字节的功能对原有的buffer处理能力进行增强）
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {
    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler){
        this.recordParser=initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler){
        //构造parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);
        parser.setOutput(new Handler<Buffer>() {
            //初始化
            int size=-1;
            //一次完整的读取（头+体）
            Buffer resultBuffer=Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if(size==-1){//读取消息头
                    //读取消息体长度
                    size=buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    //写入头信息到结果
                    resultBuffer.appendBuffer(buffer);
                }else{//读取消息体
                    //写入体信息到结果
                    resultBuffer.appendBuffer(buffer);
                    //已拼接为完整Buffer，执行处理
                    bufferHandler.handle(resultBuffer);
                    //重置一轮
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size=-1;
                    resultBuffer=Buffer.buffer();
                }
            }
        });
        return parser;
    }
}
