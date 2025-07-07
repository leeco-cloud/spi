package com.lee.spi.spi.example.example.provider;

import com.lee.spi.core.annotation.SpiProvider;

/**
 * @author yanhuai lee
 */
@SpiProvider(code = "bbb")
public class SpiTestInterfaceImpl3 extends SpiTestInterfaceImpl {

    @Override
    public String testMethod1(String name) {
        return "SpiTestInterfaceImpl3 testMethod1";
    }

    @Override
    public String testMethod(String name) {
        return "SpiTestInterfaceImpl3 testMethod";
    }

}