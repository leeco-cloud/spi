package com.lee.spi.spi.example.springTest;

import com.lee.spi.core.spring.ThreadContextBeanSelectionStrategy;
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
        ThreadContextBeanSelectionStrategy.BEAN_NAME_HOLDER.set(name);
        String s = spiTestInterface.testMethod(name);
        System.out.println(s);
        ThreadContextBeanSelectionStrategy.BEAN_NAME_HOLDER.remove();
    }

}
