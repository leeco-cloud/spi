package com.lee.spi.core.invoke;

/**
 * 身份执行器
 * @author yanhuai lee
 */
public class IdentityInvoke {

    private final String code;

    IdentityInvoke(String code) {
        this.code = code;
    }

    public <T> ExecuteInvoke<T> invoke(Class<T> spiInterface) {
        return new ExecuteInvoke<>(code, spiInterface);
    }

}
