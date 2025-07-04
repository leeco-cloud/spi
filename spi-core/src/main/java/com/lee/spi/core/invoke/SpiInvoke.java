package com.lee.spi.core.invoke;

/**
 * spi调用入口
 * @author yanhuai lee
 */
public class SpiInvoke {

    public static IdentityInvoke identity(String code) {
        return new IdentityInvoke(code);
    }

}
