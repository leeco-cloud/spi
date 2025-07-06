package com.lee.spi.core.processor;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.lee.spi.core.annotation.Spi;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.meta.SpiMeta;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Spi注解编译期执行器
 * @author yanhuai lee
 * @see com.lee.spi.core.annotation.Spi
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.lee.spi.core.annotation.Spi"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedOptions({"debug", "verify"})
public class SpiProcessor extends CommonProcessor {

    private String content;

    @Override
    protected void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<SpiMeta> spiMetas = new ArrayList<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(Spi.class)) {
            Spi spi = element.getAnnotation(Spi.class);
            // 处理加在接口上的注解
            if (element.getKind() == ElementKind.INTERFACE) {
                TypeElement interfaceElement = (TypeElement) element;
                SpiMeta spiMeta = new SpiMeta(interfaceElement.getQualifiedName().toString(), spi.code(), spi.name(), spi.desc(), spi.priority());
                spiMetas.add(spiMeta);
            }
        }
        if (!spiMetas.isEmpty()) {
            content = JSON.toJSONString(spiMetas);
        }
    }

    @Override
    protected void generateConfigFiles() {
        try{
            if (StringUtils.isBlank(content)) {
                return;
            }
            Filer filer = processingEnv.getFiler();
            Writer writer = filer.createResource(StandardLocation.CLASS_OUTPUT, "", CommonConfig.spiFilePath).openWriter();
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

}