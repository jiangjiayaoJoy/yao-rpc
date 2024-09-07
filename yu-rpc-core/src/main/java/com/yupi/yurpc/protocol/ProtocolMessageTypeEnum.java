package com.yupi.yurpc.protocol;

import lombok.Getter;

/**
 * ClassName: ProtocolMessageTypeEnum
 * Package: com.yupi.yurpc.protocol
 * Description:协议消息的类型枚举
 * 表示消息的类型：0——请求，1——响应，2——心跳，3——其他
 *
 * @Author Joy_瑶
 * @Create 2024/6/19 15:01
 * @Version 1.0
 */
@Getter
public enum ProtocolMessageTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举实例
     * @param key
     * @return
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key){
        for(ProtocolMessageTypeEnum anEnum:ProtocolMessageTypeEnum.values()){
            if(anEnum.key==key){
                return anEnum;
            }
        }
        return null;
    }
}
