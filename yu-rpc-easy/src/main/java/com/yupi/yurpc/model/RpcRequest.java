package com.yupi.yurpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: RpcRequest
 * Package: com.yupi.yurpc.model
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/7 16:20
 * @Version 1.0
 */

/**
 * 封装请求参数：将请求参数封装起来，以便后续反射调用
 * 这里的属性名称应该要与参数名称一直
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型列表
     */
    private Class<?>[] paramterTypes;
    /**
     * 参数列表
     */
    private Object[] args;
}
