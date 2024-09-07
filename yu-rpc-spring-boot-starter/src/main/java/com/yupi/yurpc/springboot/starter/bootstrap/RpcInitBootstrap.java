package com.yupi.yurpc.springboot.starter.bootstrap;

import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.server.tcp.VertxTcpServer;
import com.yupi.yurpc.springboot.starter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ClassName: RpcinitBootstrap
 * Package: com.yupi.yurpc.springboot.starter.bootstrap
 * Description:
 * Rpc框架启动
 *
 * @Author Joy_瑶
 * @Create 2024/6/29 17:33
 * @Version 1.0
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    /**
     * Spring 初始化时执行，初始化RPC框架
     *
     * @param importingClassMetadata
     * @param registry
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取EnableRpc注解的属性值
        boolean needServer = (Boolean)importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");

        //RPC框架初始化（配置和注册中心）
        RpcApplication.init();

        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //启动服务器
        if(needServer){
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        }else{
            log.info("不启动server");
        }
    }
}
