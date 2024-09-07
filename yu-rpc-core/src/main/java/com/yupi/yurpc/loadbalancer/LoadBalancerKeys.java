package com.yupi.yurpc.loadbalancer;

/**
 * ClassName: LoadBalancerKeys
 * Package: com.yupi.yurpc.loadbalancer
 * Description:
 * 负载均衡器键名常量
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 19:39
 * @Version 1.0
 */
public interface LoadBalancerKeys {
    String ROUND_ROBIN="roundRobin";
    String RANDOM="random";
    String CONSISTENT_HASH="consistentHash";
}
