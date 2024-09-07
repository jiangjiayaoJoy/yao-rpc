package com.yupi.yurpc.registry;

import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;
import io.vertx.core.impl.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: ZooKeeperRegistry
 * Package: com.yupi.yurpc.registry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/18 19:14
 * @Version 1.0
 */
@Slf4j
public class ZooKeeperRegistry implements Registry{
    private CuratorFramework client;//连接ZooKeeper的客户端
    private ServiceDiscovery<ServiceMetaInfo> serviceDiscovery;//跟ZooKeeper连接相关

    /**
     * 根节点
     */
    private static final String ZK_ROOT_PATH="/rpc/zk";

    /**
     * 本机注册的节点key集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet=new HashSet<>();
    /**
     * 本地服务缓存
     */
    private final RegistryServiceCache registryServiceCache=new RegistryServiceCache();
    /**
     * 正在监听的key集合
     */
    private final Set<String> watchingKeySet=new ConcurrentHashSet<>();

    @Override
    public void init(RegistryConfig registryConfig) {
        //创建client实例
        client = CuratorFrameworkFactory
                .builder()
                .connectString(registryConfig.getAddress())
                .retryPolicy(new ExponentialBackoffRetry(Math.toIntExact(registryConfig.getTimeout()), 3))
                .build();

        //构建serviceDiscovery实例
        serviceDiscovery= ServiceDiscoveryBuilder.builder(ServiceMetaInfo.class)
                .client(client)
                .basePath(ZK_ROOT_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
                .build();

        try {
            //启动client和serviceDiscovery
            client.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //将serviceMetaInfo服务信息注册到zk中
        serviceDiscovery.registerService(buildServiceInstance(serviceMetaInfo));

        //添加节点信息到本机注册的节点集合中
        String registerKey=ZK_ROOT_PATH+"/"+serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.add(registerKey);
    }

    private ServiceInstance<ServiceMetaInfo> buildServiceInstance(ServiceMetaInfo serviceMetaInfo) {
        String serviceAddress = serviceMetaInfo.getServiceHost() + ":" + serviceMetaInfo.getServicePort();
        try {
            return ServiceInstance
                    .<ServiceMetaInfo>builder()
                    .id(serviceAddress)
                    .name(serviceMetaInfo.getServiceKey())
                    .address(serviceAddress)
                    .payload(serviceMetaInfo)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 服务提供者主动注销服务
     * @param serviceMetaInfo
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        //从zk中移除服务
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMetaInfo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //从本地注册集合中移除服务
        String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //优先从本地缓存中获取服务
        List<ServiceMetaInfo> cachedServiceMetaInfoList = registryServiceCache.readCache();
        if(cachedServiceMetaInfoList!=null&&cachedServiceMetaInfoList.size()!=0){
            return cachedServiceMetaInfoList;
        }

        //如果本地服务缓存中没有值，就需要从zk中获取
        try {
            //从zk中查询服务信息
            Collection<ServiceInstance<ServiceMetaInfo>> serviceInstanceList = serviceDiscovery.queryForInstances(serviceKey);
            //解析服务信息
            List<ServiceMetaInfo> serviceMetaInfoList = serviceInstanceList.stream()
                    .map(ServiceInstance::getPayload)
                    .collect(Collectors.toList());
            //写入本地服务缓存
            registryServiceCache.writeCache(serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    /**
     * rpc项目终止(下线)
     */
    @Override
    public void destroy() {
        log.info("当前节点下线");
        //下线节点（这一步可以不做，因为都是临时节点，服务下线，自然就被删除掉了）
        for(String key:localRegisterNodeKeySet){
            try {
                client.delete().guaranteed().forPath(key);
            } catch (Exception e) {
                throw new RuntimeException(key+"节点下线失败");
            }
        }

        //释放资源
        if(client!=null){
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        //不需要心跳机制，建立了临时节点，如果服务器故障，则临时节点直接丢失
    }

    /**
     * 消费端进行监听：
     * 当zk中的服务信息发生变化时，会通知消费端，消费端会修改自己的本地缓存
     * @param serviceNodeKey
     */
    @Override
    public void watch(String serviceNodeKey) {
        String watchKey=ZK_ROOT_PATH+"/"+serviceNodeKey;
        boolean newWatch = watchingKeySet.add(watchKey);
        if(newWatch){
            CuratorCache curatorCache = CuratorCache.build(client, watchKey);
            curatorCache.start();
            curatorCache.listenable().addListener(
                    CuratorCacheListener
                            .builder()
                            .forDeletes(childData -> registryServiceCache.clearCache())
                            .forChanges(((oldNode,node)->registryServiceCache.clearCache()))
                            .build()
            );
        }
    }
}
