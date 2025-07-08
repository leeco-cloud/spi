package com.lee.spi.remote.rpc;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * spi remote rpc(spring)启动入口
 * @author yanhuai lee
 */
@Configurable
@ComponentScan(basePackages = {"com.lee.spi.remote.rpc"})
public class SpiRemoteRpcConfig {
}
