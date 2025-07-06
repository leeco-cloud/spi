package com.lee.spi.core.loader;

import com.alibaba.fastjson.JSON;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.meta.SpiMeta;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * spi定义元数据信息上报
 * @author yanhuai lee
 */
public class SpiLoader {

    public static List<SpiMeta> load() {
        try (InputStream inputStream = SpiLoader.class.getResourceAsStream("/" + CommonConfig.spiFilePath)) {
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
                return JSON.parseArray(content.toString(), SpiMeta.class);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("读取Spi配置失败:" + CommonConfig.spiFilePath, e);
        }
    }

}
