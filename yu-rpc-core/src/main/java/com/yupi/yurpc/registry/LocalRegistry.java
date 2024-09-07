package com.yupi.yurpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: LocalRegistry
 * Package: com.yupi.yurpc.registry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/6 21:52
 * @Version 1.0
 */
public class LocalRegistry {
    /**
     * 注册信息存储
     */
    private static final Map<String,Class<?>> map=new ConcurrentHashMap<>();

    /**
     * 注册服务
     */
    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
    }

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /**
     * 剔除服务
     * @param serviceName
     */
    public static void remove(String serviceName){
        map.remove(serviceName);
    }
}
