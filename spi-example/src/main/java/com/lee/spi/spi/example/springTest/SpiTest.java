package com.lee.spi.spi.example.springTest;

import com.lee.spi.spi.example.example.spi.SpiTestInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yanhuai lee
 */
@Component
public class SpiTest {

    @Autowired
    private SpiTestInterface spiTestInterface;

    public void test(){
        spiTestInterface.testMethod("aaa");
    }

}
