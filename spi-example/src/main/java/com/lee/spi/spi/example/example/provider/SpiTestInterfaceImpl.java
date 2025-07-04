package com.lee.spi.spi.example.example.provider;

import com.lee.spi.core.annotation.SpiProvider;
import com.lee.spi.spi.example.example.spi.SpiTestInterface1;

/**
 * @author yanhuai lee
 */
@SpiProvider(identityCode = "0")
public class SpiTestInterfaceImpl implements SpiTestInterface1 {

    @Override
    public String testMethod1(String name) {
        return "SpiTestInterfaceImpl testMethod1";
    }

    @Override
    public String testMethod(String name) {
        return "SpiTestInterfaceImpl testMethod";
    }

}
