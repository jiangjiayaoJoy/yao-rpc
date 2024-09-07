package com.yupi.yurpc.bootstrap;

import com.yupi.yurpc.RpcApplication;

/**
 * ClassName: ConsumerBootstrap
 * Package: com.yupi.yurpc.bootstrap
 * Description:
 * 服务消费者启动类（初始化）
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 20:27
 * @Version 1.0
 */
public class ConsumerBootstrap {
    public static void init(){
        //RPC框架初始化（配置和注册中心）
        RpcApplication.init();
    }
}
