package com.lee.spi.core.processor;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.lee.spi.core.annotation.Product;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.meta.ProductMeta;
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
 * Product注解编译期执行器
 * @author yanhuai lee
 * @see com.lee.spi.core.annotation.Product
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.lee.spi.core.annotation.Product"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedOptions({"debug", "verify"})
public class ProductProcessor extends CommonProcessor {

    private String content;

    @Override
    protected void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<ProductMeta> productMetaList = new ArrayList<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(Product.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                Product annotation = element.getAnnotation(Product.class);
                ProductMeta productMeta = new ProductMeta(annotation.code(), annotation.name(), annotation.desc(), annotation.priority());
                productMetaList.add(productMeta);
            }
        }
        if (!productMetaList.isEmpty()) {
            content = JSON.toJSONString(productMetaList);
        }
    }

    @Override
    protected void generateConfigFiles() {
        try{
            if (StringUtils.isBlank(content)) {
                return;
            }
            Filer filer = processingEnv.getFiler();
            Writer writer = filer.createResource(StandardLocation.CLASS_OUTPUT, "", CommonConfig.productFilePath
            ).openWriter();
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

}