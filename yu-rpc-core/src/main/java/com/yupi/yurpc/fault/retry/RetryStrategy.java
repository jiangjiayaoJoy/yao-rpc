package com.yupi.yurpc.fault.retry;

import com.yupi.yurpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * ClassName: RetryStrategy
 * Package: com.yupi.yurpc.fault.retry
 * Description:
 * 重试策略通用接口：
 * 提供一个重试方法，接受一个具体的任务参数，可以使用Callable类代表一个任务
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 21:00
 * @Version 1.0
 */
public interface RetryStrategy {
    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
