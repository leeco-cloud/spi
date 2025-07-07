package com.lee.spi.core.loader;

import com.alibaba.fastjson.JSON;
import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.exception.ErrorCode;
import com.lee.spi.core.exception.SpiRuntimeException;
import com.lee.spi.core.meta.IdentityMeta;
import com.lee.spi.core.meta.ProductMeta;
import com.lee.spi.core.meta.RelationMeta;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品叠加配置加载器
 * @author yanhuai lee
 */
public class RelationLoader {

    public static List<RelationMeta> load() {
        List<RelationMeta> result;
        try (InputStream inputStream = RelationLoader.class.getResourceAsStream("/" + CommonConfig.relationFilePath)) {
            if (inputStream == null) {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                if (StringUtils.isBlank(content.toString())) {
                    return null;
                }
                result = JSON.parseArray(content.toString(), RelationMeta.class);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("读取identity&product叠加规则配置失败" + CommonConfig.relationFilePath, e);
        }
        return checkAndSort(result);
    }

    private static List<RelationMeta> checkAndSort(List<RelationMeta> result) {
        if (result != null && !result.isEmpty()) {
            for (RelationMeta relationMeta : result) {
                // 校验业务身份
                if (SpiCache.identityMetasCache == null) {
                    throw new SpiRuntimeException(ErrorCode.UN_REGISTER_IDENTITY, relationMeta.getIdentity());
                }
                List<String> identityList = SpiCache.identityMetasCache.stream().map(IdentityMeta::getCode).collect(Collectors.toList());
                if (identityList.isEmpty()) {
                    throw new SpiRuntimeException(ErrorCode.UN_REGISTER_IDENTITY, relationMeta.getIdentity());
                }
                if (!identityList.contains(relationMeta.getIdentity())){
                    throw new SpiRuntimeException(ErrorCode.UN_REGISTER_IDENTITY, relationMeta.getIdentity());
                }
                // 校验产品身份
                List<String> productCodes = relationMeta.getProducts();
                if (productCodes.isEmpty()) {
                    continue;
                }
                for (String productCode : productCodes) {
                    if (StringUtils.isBlank(productCode)) {
                        continue;
                    }
                    if (SpiCache.productMetasCache == null) {
                        throw new SpiRuntimeException(ErrorCode.UN_REGISTER_PRODUCT, productCode);
                    }
                    List<String> productList = SpiCache.productMetasCache.stream().map(ProductMeta::getCode).collect(Collectors.toList());
                    if (productList.isEmpty()) {
                        throw new SpiRuntimeException(ErrorCode.UN_REGISTER_PRODUCT, productCode);
                    }
                    if (!productList.contains(productCode)){
                        throw new SpiRuntimeException(ErrorCode.UN_REGISTER_PRODUCT, productCode);
                    }
                }
                productCodes = sort(productCodes);
                relationMeta.setProducts(productCodes);
            }
        }
        return result;
    }

    private static List<String> sort(List<String> productCodes) {
        List<ProductMeta> sortResult = new ArrayList<>();
        for (ProductMeta productMeta : SpiCache.productMetasCache) {
            for (String productCode : productCodes) {
                if (productMeta.getCode().equals(productCode)) {
                    sortResult.add(productMeta);
                }
            }
        }
        sortResult.sort(Comparator.comparing(ProductMeta::getPriority));
        return sortResult.stream().map(ProductMeta::getCode).collect(Collectors.toList());
    }

}
