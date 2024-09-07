package com.yupi.yurpc.fault.retry;

import com.github.rholder.retry.*;
import com.yupi.yurpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: FixedIntervalRetryStrategy
 * Package: com.yupi.yurpc.fault.retry
 * Description:
 * 重试策略之固定时间重试：
 * 使用Guava-Retrying提供的RetryBuilder能够很方便地指定重试条件、重试等待策略、充实感hi停止策略、重试工作等
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 21:14
 * @Version 1.0
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)//重试条件
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))//每次重试间隔时间
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))//重试停止策略
                .withRetryListener(new RetryListener() {//监听重试，每次重试时，除了执行任务外，还能够打印当前的重试次数
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数 {}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
