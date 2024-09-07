package com.yupi.yurpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ClassName: KryoSerializer
 * Package: com.yupi.yurpc.serializer
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/8 21:09
 * @Version 1.0
 */

/**
 * Kryo序列化器
 */
public class KryoSerializer implements Serializer{
    /**
     *Kryo是线程不安全的，所以使用ThreadLocal保证每个线程有且只有一个Kryo
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL=ThreadLocal.withInitial(()->{
        Kryo kryo=new Kryo();
        //设置动态序列化和反序列化器，不提前注册所有类（可能有安全问题）
        kryo.setRegistrationRequired(false);
        return kryo;
    });
    @Override
    public <T> byte[] serialize(T obj){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        KRYO_THREAD_LOCAL.get().writeObject(output,obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> classType) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        T result = KRYO_THREAD_LOCAL.get().readObject(input, classType);
        input.close();
        return result;
    }
}
