package com.lee.spi.spi.example.example.provider;

/**
 * @author yanhuai lee
 */
public class SpiTestInterfaceImpl1 extends SpiTestInterfaceImpl {

    @Override
    public String testMethod1(String name) {
        return "SpiTestInterfaceImpl1 testMethod1";
    }

    @Override
    public String testMethod(String name) {
        return "SpiTestInterfaceImpl1 testMethod";
    }

}
