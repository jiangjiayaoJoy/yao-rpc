package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * ClassName: RandomLoadBalancer
 * Package: com.yupi.yurpc.loadbalancer
 * Description:
 * 随机负载均衡器：
 * 使用Random的nextInt(n)函数生成随机数
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 16:32
 * @Version 1.0
 */
public class RandomLoadBalancer implements LoadBalancer{
    private final Random random=new Random();
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()){
            return null;
        }
        int size=serviceMetaInfoList.size();
        if(size==1){
            return serviceMetaInfoList.get(0);
        }
        int index=random.nextInt(size);
        return serviceMetaInfoList.get(index);
    }
}
