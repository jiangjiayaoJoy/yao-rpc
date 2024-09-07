package com.yupi.yurpc.protocol;

import lombok.Getter;

/**
 * ClassName: ProtocolMessageStatusEnum
 * Package: com.yupi.yurpc.protocol
 * Description:协议消息的状态枚举
 * 表示消息响应的状态：20——请求成功，40——客户端错误，50——服务器错误
 *
 * @Author Joy_瑶
 * @Create 2024/6/19 15:42
 * @Version 1.0
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);
    private final String text;
    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举实例
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value){
        for(ProtocolMessageStatusEnum anEnum:ProtocolMessageStatusEnum.values()){
            if(anEnum.value==value){
                return anEnum;
            }
        }
        return null;
    }
}
