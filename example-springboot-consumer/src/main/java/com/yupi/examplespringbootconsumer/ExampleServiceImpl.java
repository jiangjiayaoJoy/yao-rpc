package com.yupi.examplespringbootconsumer;

import com.yupi.example.common.model.User;
import com.yupi.example.common.service.UserService;
import com.yupi.yurpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * ClassName: ExampleServiceImpl
 * Package: com.yupi.examplespringbootconsumer
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/7/1 15:27
 * @Version 1.0
 */
@Service
public class ExampleServiceImpl {
    @RpcReference
    private UserService userService;
    public void test(){
        User user=new User();
        user.setName("yaoyao");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
