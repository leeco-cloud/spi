package com.lee.spi.core.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * IdentityMeta
 * @author yanhuai lee
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpiProviderMeta {

    private String className;

    private String identityCode;

    private String desc;

    private Boolean isDefault;

}
