package com.lee.spi.core.register.processor;

import com.google.auto.service.AutoService;
import com.lee.spi.core.annotation.Identity;
import com.lee.spi.core.meta.IdentityMeta;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

/**
 * Identity注解编译期执行器
 * @author yanhuai lee
 * @see com.lee.spi.core.annotation.Identity
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.lee.spi.core.annotation.Identity"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedOptions({"debug", "verify"})
public class IdentityProcessor extends CommonProcessor {

    @Override
    protected String getSpiMetaInfoFilePath() {
        return "META-INF/services/identity/Identity.json";
    }

    @Override
    protected void processAnnotations(List<Object> metaInfo, Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Identity.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                Identity annotation = element.getAnnotation(Identity.class);
                IdentityMeta identityMeta = new IdentityMeta(annotation.code(), annotation.name(), annotation.desc(), annotation.priority());
                metaInfo.add(identityMeta);
            }
        }
    }
}