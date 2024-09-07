package com.yupi.examplespringbootconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * ClassName: ExampleServiceImplTest
 * Package: com.yupi.examplespringbootconsumer
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/7/1 15:30
 * @Version 1.0
 */
@SpringBootTest
public class ExampleServiceImplTest {
    @Resource
    private ExampleServiceImpl exampleService;
    @Test
    public void test1(){
        exampleService.test();
    }
}
