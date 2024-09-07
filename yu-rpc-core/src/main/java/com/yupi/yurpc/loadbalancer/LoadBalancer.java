package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * ClassName: LoadBalancer
 * Package: com.yupi.yurpc.loadbalancer
 * Description:负载均衡器（消费端使用）
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 16:15
 * @Version 1.0
 */
public interface LoadBalancer {
    /**
     * 选择服务调用
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 可用服务列表
     * @return
     */
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
