package com.yupi.yurpc.fault.retry;

import com.yupi.yurpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * ClassName: NoRetryStrategy
 * Package: com.yupi.yurpc.fault.retry
 * Description:
 * 重试策略之不重试：
 * 直接执行一次任务
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 21:12
 * @Version 1.0
 */
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
