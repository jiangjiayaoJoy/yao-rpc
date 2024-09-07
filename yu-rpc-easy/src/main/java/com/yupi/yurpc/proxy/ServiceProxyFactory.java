package com.yupi.yurpc.proxy;

/**
 * ClassName: ServiceProxyFactory
 * Package: com.yupi.yurpc.proxy
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/7 21:41
 * @Version 1.0
 */

import java.lang.reflect.Proxy;

/**
 * 动态代理工厂：根据指定类创建动态代理对象
 * 使用了工厂设计模式
 */
public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass){
        return (T)Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
