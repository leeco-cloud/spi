package com.lee.spi.core.loader;

import com.alibaba.fastjson.JSON;
import com.lee.spi.core.meta.IdentityMeta;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 业务身份信息上报
 * @author yanhuai lee
 */
public class IdentityLoader {

    private static final String filePath = "/META-INF/services/identity/Identity.json";

    public static volatile List<IdentityMeta> cache = null;

    public static List<IdentityMeta> load() {
        if (cache != null) {
            return cache;
        }
        synchronized (IdentityLoader.class) {
            if (cache == null) {
                synchronized (IdentityLoader.class) {
                    try (InputStream inputStream = IdentityLoader.class.getResourceAsStream(filePath)) {
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
                            cache = JSON.parseArray(content.toString(), IdentityMeta.class);
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException("读取Identity配置失败", e);
                    }
                }
            }
        }
        return cache;
    }

}
