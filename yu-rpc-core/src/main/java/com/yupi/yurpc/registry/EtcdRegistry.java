package com.yupi.yurpc.registry;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.ServiceManager;
import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import io.vertx.core.impl.ConcurrentHashSet;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * ClassName: EtcdRegistry
 * Package: com.yupi.yurpc.registry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/10 10:22
 * @Version 1.0
 */
public class EtcdRegistry implements Registry{
    private Client client;
    private KV kvClient;
    /**
     * 本机注册的节点key集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet=new HashSet<>();

    /**
     * 根节点：键在etcd中存储的根路径为/rpc/，为了区分不同项目
     */
    private static final String ETCD_ROOT_PATH="/rpc/";
    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache=new RegistryServiceCache();

    /**
     * 正在监听的节点集合
     */
    private final Set<String> watchingKeySet=new ConcurrentHashSet<>();
    /**
     * 在初始化方法中读取配置类中的信息，得到注册中心示例
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client=Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        //todo 这里的超时时间有点搞不懂是什么意思？是指连接etcd服务器超时？还是只etcd中存储的数据的超时时间？
        kvClient=client.getKVClient();
        //开启心跳检测方法
        heartBeat();
    }

    /**
     * 服务注册：创建key，并设置过期时间，value为服务注册信息的JSON序列化
     * @param serviceMetaInfo
     * @throws Exception
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建Lease和KV客户端
        Lease leaseClient = client.getLeaseClient();

        //创建一个30秒的租约
        long leaseId = leaseClient.grant(500).get().getID();

        //设置要存储的键值对
        String registerKey= ETCD_ROOT_PATH+serviceMetaInfo.getServiceNodeKey();//数据格式：/rpc/serviceName:版本号:ip地址:端口号
        ByteSequence key = ByteSequence.from(registerKey,StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).get();//将键值对存入etcd中，并设置30秒的过期时间

        //添加节点信息到本地缓存的set中
        localRegisterNodeKeySet.add(registerKey);
    }

    /**
     * 服务注销：服务提供端注销服务
     * 优先从本地缓存中获取服务；如果没有缓存，再从注册中心获取，并且设置到缓存中
     * @param serviceMetaInfo
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        try {
            String registerKey=ETCD_ROOT_PATH+serviceMetaInfo.getServiceNodeKey();
            DeleteResponse deleteResponse = kvClient.delete(ByteSequence.from(registerKey,StandardCharsets.UTF_8)).get();
            //也要从本地缓存中删除
            localRegisterNodeKeySet.remove(registerKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务发现：服务消费者去etcd中发现服务，将服务名称作为前缀，从etcd获取服务下的节点列表
     * @param serviceKey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //优先从缓存中获取服务
        List<ServiceMetaInfo> cacheServiceMetaInfoList = registryServiceCache.readCache();
        if(cacheServiceMetaInfoList!=null&&cacheServiceMetaInfoList.size()!=0){
            return cacheServiceMetaInfoList;
        }

        //前缀搜索，结尾一定要加'/'
        String searchPrefix=ETCD_ROOT_PATH+serviceKey;

        try {
            //前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
//            //解析服务信息
//            return keyValues.stream()
//                    .map(keyValue -> {
//                        String value=keyValue.getValue().toString(StandardCharsets.UTF_8);
//                        return JSONUtil.toBean(value,ServiceMetaInfo.class);
//                    })
//                    .collect(Collectors.toList());
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        //监听key的变化
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
            //写入服务缓存
            registryServiceCache.writeCache(serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }

    }

    /**
     * 注册中心销毁：用于项目关闭后释放资源
     */
    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        //删除注册中心中的节点
        for(String key:localRegisterNodeKeySet){
            try {
                DeleteResponse deleteResponse = kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key+"节点下线失败");
            }
        }
        //释放资源
        if(kvClient!=null){
            kvClient.close();
        }
        if(client!=null){
            client.close();
        }
    }

    /**
     * 心跳检测：每隔10秒重新注册服务
     * 因为etcd注册中心的节点有过期机制，所以对于心跳检测：
     * 我们只需要在服务提供者这边实现隔一段时间重新注册即可；
     * 如果没有重新注册，节点过期时间到了，就会自动删除，那么就代表该节点已下线
     */
    @Override
    public void heartBeat() {
        //10秒续签一次
        CronUtil.schedule("*/10 * * * * *",new Task(){
            @Override
            public void execute() {
                //遍历本地缓存中的所有已注册节点
                for(String key:localRegisterNodeKeySet){
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        //该节点已过期（需要重启节点才能重新注册）
                        if(keyValues.isEmpty()){
                            continue;
                        }
                        //节点未过期，获取节点信息并重新注册
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key+"续签失败",e);
                    }
                }
            }
        });
        //支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 监听（消费端）
     * @param serviceNodeKey
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        //之前未被监听，开启监听;之前已监听，就什么都不做
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if(newWatch){
            watchClient.watch(ByteSequence.from(serviceNodeKey,StandardCharsets.UTF_8),response->{
                for(WatchEvent event:response.getEvents()){
                    //监听该结点发生变化后，如果是删除则也删除对应的本地缓存
                    switch(event.getEventType()){
                        case DELETE:
                            registryServiceCache.clearCache();
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }
}
