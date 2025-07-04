package com.lee.spi.core.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * SpiMeta
 * @author yanhuai lee
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpiMeta {

    private String interfaceName;

    private String code;

    private String name;

    private String desc;

    private Integer priority;

}
