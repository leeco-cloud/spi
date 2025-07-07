package com.lee.spi.core.loader;

import com.alibaba.fastjson.JSON;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.meta.ProductMeta;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 产品身份信息上报
 * @author yanhuai lee
 */
public class ProductLoader {

    public static List<ProductMeta> load() {
        try (InputStream inputStream = ProductLoader.class.getResourceAsStream("/" + CommonConfig.productFilePath)) {
            if (inputStream == null) {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                if (StringUtils.isBlank(content.toString())){
                    return null;
                }
                return JSON.parseArray(content.toString(), ProductMeta.class);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("读取product配置失败" + CommonConfig.productFilePath, e);
        }
    }

}
