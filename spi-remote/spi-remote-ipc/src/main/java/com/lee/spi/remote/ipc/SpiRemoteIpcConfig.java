package com.lee.spi.remote.ipc;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * spi remote ipc(spring)启动入口
 * @author yanhuai lee
 */
@Configurable
@ComponentScan(basePackages = {"com.lee.spi.remote.ipc"})
public class SpiRemoteIpcConfig {
}
