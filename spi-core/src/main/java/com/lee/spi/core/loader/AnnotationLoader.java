package com.lee.spi.core.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解信息采集
 * @author yanhuai lee
 */
public class AnnotationLoader {

    public static List<AnnotationData> load(String annotation) {
        List<AnnotationData> data = new ArrayList<>();
        try (InputStream is = AnnotationLoader.class.getClassLoader()
                .getResourceAsStream("META-INF/" + annotation + ".info");
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    data.add(new AnnotationData(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2])
                    ));
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to load annotation data", e);
        }
        return data;
    }

    public static class AnnotationData {
        public final String className;
        public final String value;
        public final int priority;

        public AnnotationData(String className, String value, int priority) {
            this.className = className;
            this.value = value;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s (priority=%d)",
                    className, value, priority);
        }
    }
}
