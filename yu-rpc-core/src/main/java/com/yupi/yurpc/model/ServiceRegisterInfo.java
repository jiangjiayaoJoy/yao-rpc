package com.yupi.yurpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: ServiceRegisterInfo
 * Package: com.yupi.yurpc.model
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 19:56
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegisterInfo<T> {
    private String serviceName;
    private Class<? extends T> implClass;
}
