package com.yupi.example.provider;

/**
 * ClassName: ProviderExample
 * Package: com.yupi.example.provider
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/8 13:05
 * @Version 1.0
 */

import com.yupi.example.common.service.UserService;
import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.bootstrap.ProviderBootstrap;
import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.model.ServiceRegisterInfo;
import com.yupi.yurpc.registry.LocalRegistry;
import com.yupi.yurpc.registry.Registry;
import com.yupi.yurpc.registry.RegistryFactory;
import com.yupi.yurpc.server.VertxHttpServer;
import com.yupi.yurpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者实例
 */
public class ProviderExample {
    public static void main(String[] args) {
        //要注册的服务
        List<ServiceRegisterInfo> serviceRegisterInfoList=new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        //启动服务提供者类
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
