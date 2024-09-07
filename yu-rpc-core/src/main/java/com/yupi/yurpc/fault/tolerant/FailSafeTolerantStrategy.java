package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * ClassName: FailSafeTolerantStrategy
 * Package: com.yupi.yurpc.fault.tolerant
 * Description:
 * 容错策略——静默处理（当作没有错误发生）
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 16:30
 * @Version 1.0
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常",e);
        return new RpcResponse();
    }
}
