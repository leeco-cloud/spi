package com.lee.spi.spi.example;

import com.alibaba.fastjson.JSON;
import com.lee.spi.core.loader.IdentityLoader;
import com.lee.spi.core.loader.SpiLoader;
import com.lee.spi.spi.example.example.SpiTestInterface;
import com.lee.spi.spi.example.example.SpiTestInterface2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

@SpringBootApplication
@ComponentScan({"com.lee.spi"})
public class SpiExampleApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(SpiExampleApplication.class, args);
//    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(IdentityLoader.load()));

        System.out.println();

        System.out.println(JSON.toJSONString(SpiLoader.load()));


        Method[] methods = SpiTestInterface.class.getMethods();
        for (Method method : methods) {
            for (Type genericParameterType : method.getGenericParameterTypes()) {
                System.out.println(genericParameterType.getTypeName().replaceAll(" ",""));
            }
        }

        System.out.println();

        methods = SpiTestInterface2.class.getMethods();
        for (Method method : methods) {
            for (Type genericParameterType : method.getGenericParameterTypes()) {
                System.out.println(genericParameterType.getTypeName().replaceAll(" ",""));
            }
        }

    }

}
