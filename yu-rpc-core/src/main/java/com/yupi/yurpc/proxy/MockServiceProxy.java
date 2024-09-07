package com.yupi.yurpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ClassName: MockServiceProxy
 * Package: com.yupi.yurpc.proxy
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/8 16:49
 * @Version 1.0
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    /**
     * 调用代理
     * @param proxy the proxy instance that the method was invoked on
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //Mock服务的代理对象需要做的逻辑：
        //给调用Mock接口的消费者返回一个固定值
        //根据方法的返回值类型，生成特定的默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 根据指定类型，生成其默认值对象
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type){
        //todo 可完善，支持更多返回类型的默认值生成(使用Faker之类的伪造数据生成库，来生成默认值)
        //基本类型
        if(type.isPrimitive()){
            if(type==byte.class){
                return (byte)0;
            }else if(type==short.class){
                return (short)0;
            }else if(type==int.class){
                return 0;
            }else if(type==long.class){
                return 0L;
            }else if(type==float.class){
                return (float)0.0;
            }else if(type==double.class){
                return 0.0;
            }else if(type==char.class){
                return ' ';
            }else if(type==boolean.class){
                return false;
            }
        }
        //引用数据类型和数组返回null
        return null;
    }
}
