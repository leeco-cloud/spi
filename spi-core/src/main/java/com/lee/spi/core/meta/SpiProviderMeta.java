package com.lee.spi.core.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * SpiProviderMeta
 * @author yanhuai lee
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpiProviderMeta {

    private String className;

    private Class<?> classType;

    private String interFaceName;

    private Class<?> interFaceNameClassType;

    private String code;

    private String desc;

    private Boolean isDefault;

}
