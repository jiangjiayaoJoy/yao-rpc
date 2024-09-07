package com.yupi.yurpc.springboot.starter.annotation;

import com.yupi.yurpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import com.yupi.yurpc.springboot.starter.bootstrap.RpcInitBootstrap;
import com.yupi.yurpc.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: EnableRpc
 * Package: com.yupi.yurpc.springboot.starter.annotation
 * Description:
 * 启用Rpc注解：
 * 用于全局标识项目需要引入RPC框架、执行RPC初始化方法
 *
 * @Author Joy_瑶
 * @Create 2024/6/29 17:16
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class,RpcConsumerBootstrap.class})
public @interface EnableRpc {
    /**
     * 是否需要启动server
     * @return
     */
    boolean needServer() default true;
}
