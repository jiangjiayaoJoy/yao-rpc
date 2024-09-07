package com.yupi.yurpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: ProtocolMessageSerializerEnum
 * Package: com.yupi.yurpc.protocol
 * Description:消息的序列器类型枚举
 * 0——jdk,1——json,2——kryo,3——hessian
 *
 * @Author Joy_瑶
 * @Create 2024/6/19 15:51
 * @Version 1.0
 */
@Getter
public enum ProtocolMessageSerializerEnum {
    JDK(0,"jdk"),
    JSON(1,"json"),
    KRYO(2,"kryo"),
    HESSIAN(3,"hessian");
    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值列表
     * @return
     */
    public static List<String> getValues(){
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

    /**
     * 根据key获取枚举实例
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key){
        for(ProtocolMessageSerializerEnum anEnum:ProtocolMessageSerializerEnum.values()){
            if(anEnum.key==key){
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举实例
     * @param value
     * @return
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        for(ProtocolMessageSerializerEnum anEnum:ProtocolMessageSerializerEnum.values()){
            if(anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}
