package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.model.RpcResponse;

import java.util.Map;

/**
 * ClassName: TolerantStrategy
 * Package: com.yupi.yurpc.fault.tolerant
 * Description:
 * 容错策略通用接口
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 16:26
 * @Version 1.0
 */
public interface TolerantStrategy {
    /**
     * 容错
     * @param context 上下文，用于传递数据
     * @param e       异常
     * @return
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e);
}
