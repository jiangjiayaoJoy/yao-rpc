package com.yupi.yurpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: RpcResponse
 * Package: com.yupi.yurpc.model
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/7 16:20
 * @Version 1.0
 */

/**
 * 封装调用服务方法所返回的参数、以及调用的信息（比如异常情况）等。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    /**
     * 响应数据
     */
    private Object data;
    /**
     * 响应数据类型（预留）
     */
    private Class<?> dataType;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 异常信息
     */
    private Exception exception;
}
