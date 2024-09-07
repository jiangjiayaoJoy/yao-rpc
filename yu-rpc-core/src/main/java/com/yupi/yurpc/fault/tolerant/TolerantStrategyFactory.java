package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.spi.SpiLoader;

/**
 * ClassName: TolerantStrategyFactory
 * Package: com.yupi.yurpc.fault.tolerant
 * Description:
 * 容错策略工厂（工厂模式，用于获取容错策略对象）
 *
 * @Author Joy_瑶
 * @Create 2024/6/28 16:44
 * @Version 1.0
 */
public class TolerantStrategyFactory {
    static{
        SpiLoader.load(TolerantStrategy.class);
    }

    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY=new FailFastTolerantStrategy();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class,key);
    }
}
