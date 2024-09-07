package com.yupi.yurpc.utils;

/**
 * ClassName: ConfigUtils
 * Package: com.yupi.yurpc.utils
 * Description:
 *
 * @Author Joy_瑶
 * @Create 2024/6/8 12:00
 * @Version 1.0
 */

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 配置工具类：读取配置文件并返回配置对象
 */
public class ConfigUtils {
    /**
     * 加载配置对象
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass,String prefix){
        return loadConfig(tClass,prefix,"");
    }

    /**
     * 加载配置对象，支持区分环境
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass,String prefix,String environment){
        StringBuilder configFileBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");//application-environment.properties
        Props props = new Props(configFileBuilder.toString());//配置文件名字为configFileBuilder，将该配置文件加载为Props类
        return props.toBean(tClass,prefix);//将Props类转为tClass类，并创建对象返回
    }
}
