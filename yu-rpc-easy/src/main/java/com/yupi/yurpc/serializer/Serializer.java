package com.yupi.yurpc.serializer;

import java.io.IOException;

/**
 * ClassName: Serializer
 * Package: com.yupi.yurpc.serializer
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/6 22:15
 * @Version 1.0
 */
public interface Serializer {
    /**
     * 序列化
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    <T> T deserialize(byte[] bytes,Class<T> type) throws IOException;
}
