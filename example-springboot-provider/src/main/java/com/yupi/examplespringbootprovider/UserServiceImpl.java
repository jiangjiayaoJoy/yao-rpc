package com.yupi.examplespringbootprovider;

import com.yupi.example.common.model.User;
import com.yupi.example.common.service.UserService;
import com.yupi.yurpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * ClassName: UserServiceImpl
 * Package: com.yupi.examplespringbootprovider
 * Description:
 * 用户服务实现类
 *
 * @Author Joy_瑶
 * @Create 2024/7/1 15:23
 * @Version 1.0
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getName());
        return user;
    }
}
