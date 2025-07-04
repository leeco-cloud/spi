package com.lee.spi.spi.example.example.provider;

import com.lee.spi.core.annotation.SpiProvider;

/**
 * @author yanhuai lee
 */
@SpiProvider(identityCode = "aaa")
public class SpiTestInterfaceImpl2 extends SpiTestInterfaceImpl1 {

    @Override
    public String testMethod1(String name) {
        return "SpiTestInterfaceImpl2 testMethod1";
    }

    @Override
    public String testMethod(String name) {
        return "SpiTestInterfaceImpl2 testMethod";
    }

}
