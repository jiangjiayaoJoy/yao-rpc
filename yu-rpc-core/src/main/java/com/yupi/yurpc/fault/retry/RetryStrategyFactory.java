package com.yupi.yurpc.fault.retry;

import com.yupi.yurpc.spi.SpiLoader;

/**
 * ClassName: RetryStrategyFactory
 * Package: com.yupi.yurpc.fault.retry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 21:34
 * @Version 1.0
 */
public class RetryStrategyFactory {
    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试器
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY=new NoRetryStrategy();
    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }
}
