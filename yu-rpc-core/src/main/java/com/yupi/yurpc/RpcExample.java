package com.yupi.yurpc;

import com.yupi.yurpc.serializer.Serializer;
import com.yupi.yurpc.spi.SpiLoader;

/**
 * ClassName: RpcExample
 * Package: com.yupi.yurpc
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/9 11:28
 * @Version 1.0
 */
public class RpcExample {
    public static void main(String[] args) {
        SpiLoader.load(Serializer.class);
    }
}
