package com.yupi.yurpc.registry;

/**
 * ClassName: RegistryServiceCache
 * Package: com.yupi.yurpc.registry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/11 11:36
 * @Version 1.0
 */

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心服务本地缓存
 */
public class RegistryServiceCache {
    /**
     * 服务缓存列表
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param newServiceCache
     */
    void writeCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceCache=newServiceCache;
    }

    /**
     * 都读缓存
     * @return
     */
    List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache(){
        this.serviceCache=null;
    }
}
