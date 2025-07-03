package com.lee.spi.core;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * spi启动入口
 * @author yanhuai lee
 */
@Configurable
@ComponentScan(basePackages = {"com.lee.spi.spi.core"})
public class SpiConfig {

}
