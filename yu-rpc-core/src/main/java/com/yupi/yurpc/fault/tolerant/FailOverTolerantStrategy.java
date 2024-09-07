package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.model.RpcResponse;

import java.util.Map;

/**
 * ClassName: FailOverTolerantStrategy
 * Package: com.yupi.yurpc.fault.tolerant
 * Description:
 * 容错策略——故障转移（将请求转移到其他没有出现故障的节点）
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 16:36
 * @Version 1.0
 */
public class FailOverTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 待扩展，获取其他服务节点并调用
        //可以利用容错方法中的上下文参数传递所有的服务节点和本次调用的服务节点，选择一个其他节点再次发起调用
        return null;
    }
}
