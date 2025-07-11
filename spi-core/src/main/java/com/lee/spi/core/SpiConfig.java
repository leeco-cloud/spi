package com.lee.spi.core;

import com.lee.spi.core.spring.SpiBeanPostProcessor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * spi(spring)启动入口
 * @author yanhuai lee
 */
@Configurable
@ComponentScan(basePackages = {"com.lee.spi.core"})
public class SpiConfig {

    @Bean
    public SpiBeanPostProcessor spiBeanPostProcessor() {
        return new SpiBeanPostProcessor();
    }

}
