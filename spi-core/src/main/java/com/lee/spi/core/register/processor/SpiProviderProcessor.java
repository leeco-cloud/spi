package com.lee.spi.core.register.processor;

import com.google.auto.service.AutoService;
import com.lee.spi.core.annotation.Spi;
import com.lee.spi.core.annotation.SpiProvider;
import com.lee.spi.core.meta.SpiProviderMeta;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

/**
 * Spi注解编译期执行器
 * @author yanhuai lee
 * @see Spi
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.lee.spi.core.annotation.SpiProvider"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedOptions({"debug", "verify"})
public class SpiProviderProcessor extends CommonProcessor {

    @Override
    protected String getSpiMetaInfoFilePath() {
        return "META-INF/services/SpiProvider.json";
    }

    @Override
    protected void processAnnotations(List<Object> metaInfo, Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiProvider.class)) {
            SpiProvider spiProvider = element.getAnnotation(SpiProvider.class);
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement elementImpl = (TypeElement) element;
                List<? extends TypeMirror> interfaces = elementImpl.getInterfaces();
                if (!interfaces.isEmpty()) {
                    for (TypeMirror typeMirror : interfaces) {
                        Spi spi = typeMirror.getAnnotation(Spi.class);

                    }
                }
                String classFullName = getBinaryNameImpl(elementImpl, elementImpl.getSimpleName().toString());
                SpiProviderMeta spiProviderMeta = new SpiProviderMeta(classFullName, spiProvider.identityCode(), spiProvider.desc(), spiProvider.isDefault());
                metaInfo.add(spiProviderMeta);
            }
        }
    }

    /**
     * spi实现为匿名内部类
     */
    private String getBinaryNameImpl(TypeElement element, String className) {
        Element enclosingElement = element.getEnclosingElement();

        if (enclosingElement instanceof PackageElement) {
            PackageElement pkg = (PackageElement) enclosingElement;
            if (pkg.isUnnamed()) {
                return className;
            }
            return pkg.getQualifiedName() + "." + className;
        }

        TypeElement typeElement = (TypeElement) enclosingElement;
        return getBinaryNameImpl(typeElement, typeElement.getSimpleName() + "$" + className);
    }


}