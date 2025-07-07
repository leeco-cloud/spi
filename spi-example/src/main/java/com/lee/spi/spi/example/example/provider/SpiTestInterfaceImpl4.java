package com.lee.spi.spi.example.example.provider;

import com.lee.spi.core.annotation.SpiProvider;
import com.lee.spi.spi.example.example.spi.SpiTestInterface2;

/**
 * @author yanhuai lee
 */
@SpiProvider(code = "ccc", isDefault = true)
public class SpiTestInterfaceImpl4 extends SpiTestInterfaceImpl implements SpiTestInterface2 {

    @Override
    public String testMethod1(String name) {
        return "SpiTestInterfaceImpl4 testMethod1";
    }

    @Override
    public String testMethod(String name) {
        return "SpiTestInterfaceImpl4 testMethod";
    }

    @Override
    public String testMethod2(String name) {
        return "SpiTestInterfaceImpl4 testMethod2";
    }
}
