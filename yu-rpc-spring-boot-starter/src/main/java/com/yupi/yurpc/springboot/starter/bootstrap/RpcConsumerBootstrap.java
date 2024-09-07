package com.yupi.yurpc.springboot.starter.bootstrap;

import com.yupi.yurpc.proxy.ServiceProxyFactory;
import com.yupi.yurpc.springboot.starter.annotation.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * ClassName: RpcConsumerBootstrap
 * Package: com.yupi.yurpc.springboot.starter.bootstrap
 * Description:
 * Rpc服务消费者启动类
 *
 * @Author Joy_瑶
 * @Create 2024/6/29 18:08
 * @Version 1.0
 */
public class RpcConsumerBootstrap implements BeanPostProcessor {
    /**
     * Bean初始化后执行，注入服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        //遍历对象的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for(Field field:declaredFields){
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if(rpcReference!=null){
                //为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if(interfaceClass==void.class){
                    interfaceClass=field.getType();
                }
                field.setAccessible(true);
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try{
                    field.set(bean,proxyObject);
                    field.setAccessible(false);
                }catch (IllegalAccessException e){
                    throw new RuntimeException("为字段注入代理对象失败",e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean,beanName);
    }
}
