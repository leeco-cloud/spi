package com.lee.spi.spi.example.example;

import com.lee.spi.core.annotation.Spi;

import java.util.Map;

/**
 * @author yanhuai lee
 */
@Spi
public interface SpiTestInterface {

    default void testMethod(String name, Map map){

    }

}
