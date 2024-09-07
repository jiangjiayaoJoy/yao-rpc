package com.yupi.yurpc.registry;

/**
 * ClassName: Registry
 * Package: com.yupi.yurpc.registry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/10 10:14
 * @Version 1.0
 */

import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心接口：后续可以实现多种不同的注册中心实现类，使用SPI机制动态加载
 * 提供初始化、服务注册、服务注销、服务发现、服务销毁等方法
 */
public interface Registry {
    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);
    /**
     * 服务注册（服务提供端）
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务销毁（服务提供端）
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现（获取某服务的所有节点，服务消费端）
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 注册中心销毁
     */
    void destroy();

    /**
     * 心跳检测（对服务端进行）
     */
    void heartBeat();

    /**
     * 监听（消费端）
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);
}
