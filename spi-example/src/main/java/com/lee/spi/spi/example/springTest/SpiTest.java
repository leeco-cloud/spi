package com.lee.spi.spi.example.springTest;

import com.lee.spi.core.spring.BizSession;
import com.lee.spi.spi.example.example.spi.SpiTestInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yanhuai lee
 */
@Component
public class SpiTest {

    @Autowired
    private SpiTestInterface spiTestInterface;

    public void test(String name){
        BizSession.IDENTITY.set(name);
        String s = spiTestInterface.testMethod(name);
        System.out.println(s);
        BizSession.IDENTITY.remove();
    }

}
