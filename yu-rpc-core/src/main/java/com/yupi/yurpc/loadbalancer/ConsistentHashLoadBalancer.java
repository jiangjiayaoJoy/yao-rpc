package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ClassName: ConsistentHashLoadBalancer
 * Package: com.yupi.yurpc.loadbalancer
 * Description:
 * 一致性哈希负载均衡器：
 * 使用TreeMap实现一致性哈希环，因为它的两个方法较为方便：ceilingEntry和firstEntry
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 16:37
 * @Version 1.0
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{
    /**
     * 一致性哈希环，存放虚拟节点
     */
    private final TreeMap<Integer,ServiceMetaInfo> virtualNodes=new TreeMap<>();//哈希值->节点信息
    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM=100;//每一个真实节点负责100个虚拟节点
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()){
            return null;
        }

        //构建虚拟节点环
        for(ServiceMetaInfo serviceMetaInfo:serviceMetaInfoList){
            for(int i=0;i<VIRTUAL_NODE_NUM;i++){
                int hash=getHash(serviceMetaInfo.getServiceAddress()+"#"+i);
                virtualNodes.put(hash,serviceMetaInfo);
            }
        }

        //获取调用请求的hash值
        int hash=getHash(requestParams);

        //选择大于或等于调用请求hash值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if(entry==null){
            entry=virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * Hash算法：
     * 这里只是简单地调用了Object的hashCode
     * todo 后续可以实现更加复杂的hash算法
     * @param key
     * @return
     */
    private int getHash(Object key){
        return key.hashCode();
    }
}
