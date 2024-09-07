package com.yupi.yurpc.springboot.starter.annotation;

import com.yupi.yurpc.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: RpcService
 * Package: com.yupi.yurpc.springboot.starter.annotation
 * Description:
 * 服务提供者注解（用于注册服务）:
 * 需要指定服务的注册信息，如服务接口实现类、版本号、服务名称等。
 *
 * @Author Joy_瑶
 * @Create 2024/6/29 17:16
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
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
}
