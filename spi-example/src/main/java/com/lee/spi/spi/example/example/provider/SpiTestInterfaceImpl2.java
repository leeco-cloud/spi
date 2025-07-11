package com.lee.spi.spi.example.example.provider;

import com.lee.spi.core.annotation.SpiProvider;

/**
 * @author yanhuai lee
 */
@SpiProvider(code = "bizCode")
public class SpiTestInterfaceImpl2 extends SpiTestInterfaceImpl1 {

    @Override
    public String testMethod1(String name) {
        System.out.println("bizCode SpiTestInterfaceImpl2::testMethod1");
        return "SpiTestInterfaceImpl2 testMethod1";
    }

    @Override
    public String testMethod(String name) {
        System.out.println("bizCode SpiTestInterfaceImpl2::testMethod");
        return "SpiTestInterfaceImpl2 testMethod";
    }

}
