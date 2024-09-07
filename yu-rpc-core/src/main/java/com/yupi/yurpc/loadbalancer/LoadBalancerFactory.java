package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.spi.SpiLoader;

/**
 * ClassName: LoadBalancerFactory
 * Package: com.yupi.yurpc.loadbalancer
 * Description:
 * 负载均衡器工厂：
 * 使用工厂模式，支持根据服务提供者名字来获取服务提供者类实例
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 19:30
 * @Version 1.0
 */
public class LoadBalancerFactory {
    static{
        SpiLoader.load(LoadBalancer.class);//工厂类一加载，就会将该工厂对应的所有服务提供者类实例都创建并保存在SpiLoader中
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER=new RoundRobinLoadBalancer();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class,key);
    }
}
