package com.lee.spi.spi.example.example;

import com.lee.spi.core.annotation.Spi;

import java.util.Map;

/**
 * @author yanhuai lee
 */
@Spi
public interface SpiTestInterface2 {

    void testMethod2(String name, Map<String, String> map);

    void testMethod22(String name);

}
