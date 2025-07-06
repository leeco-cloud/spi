package com.lee.spi.spi.example;

import com.lee.spi.core.invoke.SpiInvoke;
import com.lee.spi.spi.example.example.spi.SpiTestInterface;
import com.lee.spi.spi.example.example.spi.SpiTestInterface1;
import com.lee.spi.spi.example.example.spi.SpiTestInterface2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.lee.spi"})
public class SpiExampleApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(SpiExampleApplication.class, args);
//    }
//
    public static void main(String[] args) {
        try {
            String result = SpiInvoke
                    .identity("ccc")
                    .invoke(SpiTestInterface.class)
                    .executeGetResult(service -> service.testMethod("0"));
            System.out.println(result);

            result = SpiInvoke
                    .identity("ccc")
                    .invoke(SpiTestInterface2.class)
                    .executeGetResult(service -> service.testMethod2("0"));
            System.out.println(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
