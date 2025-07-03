package com.lee.spi.spi.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan({"com.lee.spi"})
public class SpiExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiExampleApplication.class, args);
    }

}
