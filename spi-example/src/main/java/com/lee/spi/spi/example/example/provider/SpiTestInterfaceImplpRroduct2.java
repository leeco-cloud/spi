package com.lee.spi.spi.example.example.provider;

import com.lee.spi.core.annotation.SpiProvider;
import com.lee.spi.spi.example.example.spi.SpiTestInterface1;

/**
 * @author yanhuai lee
 */
@SpiProvider(code = "product2")
public class SpiTestInterfaceImplpRroduct2 implements SpiTestInterface1 {

    @Override
    public String testMethod1(String name) {
        System.out.println("产品叠加product2 testMethod1");
        return "SpiTestInterfaceImplpRroduct2 testMethod1";
    }

    @Override
    public String testMethod(String name) {
        System.out.println("产品叠加product2 testMethod");
        return "SpiTestInterfaceImplpRroduct2 testMethod";
    }

}
