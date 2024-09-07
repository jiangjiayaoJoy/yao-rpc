package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.model.RpcResponse;

import java.util.Map;

/**
 * ClassName: FailBackTolerantStrategy
 * Package: com.yupi.yurpc.fault.tolerant
 * Description:
 * 容错策略——故障恢复（降级调用其他服务）
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 16:32
 * @Version 1.0
 */
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 可自行扩展，获取降级的服务并调用
        //思路，可以考虑采用Dubbo的Mock能力，让消费端指定调用失败后要执行的本地服务和方法
        return null;
    }
}
