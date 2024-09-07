package com.yupi.yurpc.registry;

import com.yupi.yurpc.spi.SpiLoader;

/**
 * ClassName: RegistryFactory
 * Package: com.yupi.yurpc.registry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/10 11:18
 * @Version 1.0
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY=new EtcdRegistry();

    /**
     * 获取示例
     * @param key
     * @return
     */
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
