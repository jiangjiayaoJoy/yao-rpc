package com.yupi.yurpc.springboot.starter.annotation;

import com.yupi.yurpc.constant.RpcConstant;
import com.yupi.yurpc.fault.retry.RetryStrategyKeys;
import com.yupi.yurpc.fault.tolerant.TolerantStrategyKeys;
import com.yupi.yurpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: RpcReference
 * Package: com.yupi.yurpc.springboot.starter.annotation
 * Description:
 * 服务消费者注解（用于注入服务）：
 * 在需要注入服务代理对象的属性上使用
 * 需要指定调用服务相关的属性，比如服务接口类、版本号、负载均衡器、重试策略、是否Mock模拟调用等
 *
 * @Author Joy_瑶
 * @Create 2024/6/29 17:16
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcReference {
    /**
     * 服务接口类
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     * @return
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     * @return
     */
    String loadBalance() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     * @return
     */
    String retryStrategy() default RetryStrategyKeys.NO;

    /**
     * 容错策略
     * @return
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    /**
     * 模拟调用
     * @return
     */
    boolean mock() default false;
}
