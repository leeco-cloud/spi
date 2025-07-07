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
public class ProductMeta {

    private String code;

    private String name;

    private String desc;

    private Integer priority;

}
