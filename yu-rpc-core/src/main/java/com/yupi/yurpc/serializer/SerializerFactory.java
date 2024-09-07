package com.yupi.yurpc.serializer;

/**
 * ClassName: SerializerFactory
 * Package: com.yupi.yurpc.serializer
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/8 21:21
 * @Version 1.0
 */

import com.yupi.yurpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂（用于获取序列化器对象）
 */
public class SerializerFactory {
    /**
     //     * 序列化映射（用于实现单例）
     //     */
//    private static final Map<String,Serializer> KEY_SERIALIZER_MAP=new HashMap<String,Serializer>(){
//        {
//            put(SerializerKeys.JDK,new JdkSerializer());
//            put(SerializerKeys.JSON,new JsonSerializer());
//            put(SerializerKeys.KRYO,new KryoSerializer());
//            put(SerializerKeys.HESSIAN,new HessianSerializer());
//        }
//    };
//    private static final Serializer DEFAULT_SERIALIZER=KEY_SERIALIZER_MAP.get("jdk");
//    /**
//     * 获取实例
//     * @param key
//     * @return
//     */
//    public static Serializer getInstance(String key){
//        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
//    }

    /**
     * 使用静态代码块，在工厂首次加载时，就会调用 Spiloader 的 load 方法加载序列化器接口的所有实现类，之后就可以
     * 通过调用 getlnstance 方法获取指定的实现类对象了。
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
