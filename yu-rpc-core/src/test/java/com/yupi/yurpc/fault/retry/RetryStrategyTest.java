package com.yupi.yurpc.fault.retry;

import com.yupi.yurpc.model.RpcResponse;
import org.junit.Test;

/**
 * ClassName: RetryStrategyTest
 * Package: com.yupi.yurpc.fault.retry
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/26 21:24
 * @Version 1.0
 */
public class RetryStrategyTest {
    RetryStrategy retryStrategy=new FixedIntervalRetryStrategy();


    @Test
    public void doRetry(){

        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("测试调用失败！");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败！");
            e.printStackTrace();
        }
    }
}
