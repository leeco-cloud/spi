package com.lee.spi.core.util;

import com.lee.spi.core.spring.SpiBeanPostProcessor;

/**
 * 环境工具类
 * @author yanhuai lee
 */
public class EnvUtils {

    public static boolean isSpringEnvironment() {
        return SpiBeanPostProcessor.isSpringEnv.get();
    }

}
