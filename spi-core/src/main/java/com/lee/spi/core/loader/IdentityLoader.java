package com.lee.spi.core.loader;

import com.alibaba.fastjson.JSON;
import com.lee.spi.core.config.CommonConfig;
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

    public static List<IdentityMeta> load() {
        try (InputStream inputStream = IdentityLoader.class.getResourceAsStream("/" + CommonConfig.identityFilePath)) {
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
                return JSON.parseArray(content.toString(), IdentityMeta.class);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("读取Identity配置失败" + CommonConfig.identityFilePath, e);
        }
    }

}
