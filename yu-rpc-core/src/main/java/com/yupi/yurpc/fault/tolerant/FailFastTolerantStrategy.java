package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.model.RpcResponse;

import java.util.Map;

/**
 * ClassName: FailFastTolerantStrategy
 * Package: com.yupi.yurpc.fault.tolerant
 * Description:
 * 容错策略——快速失败(立刻把错误抛给外层调用方)
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 16:28
 * @Version 1.0
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务错误",e);
    }
}
