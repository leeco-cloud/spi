package com.lee.spi.core.processor;

import com.google.auto.service.AutoService;
import com.lee.spi.core.annotation.Spi;
import com.lee.spi.core.annotation.SpiProvider;
import com.lee.spi.core.util.ProcessorUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.Writer;
import java.util.*;

/**
 * SpiProvider注解编译期执行器
 * @author yanhuai lee
 * @see com.lee.spi.core.annotation.SpiProvider
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.lee.spi.core.annotation.SpiProvider"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedOptions({"debug", "verify"})
public class SpiProviderProcessor extends CommonProcessor {

    private final Map<String, List<String>> cache = new HashMap<>();;

    @Override
    protected void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SpiProvider.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement elementImpl = (TypeElement) element;

                ProcessorUtils processorUtils = new ProcessorUtils(processingEnv);
                Set<TypeElement> allInterfaces = processorUtils.getAllInterfaces(elementImpl);

                if (!allInterfaces.isEmpty()) {
                    for (TypeElement typeMirror : allInterfaces) {
                        Spi spi = typeMirror.getAnnotation(Spi.class);
                        if (spi != null) {
                            List<String> cacheOrDefault = cache.getOrDefault(typeMirror.getQualifiedName().toString(), new ArrayList<>());
                            String classFullName = getBinaryNameImpl(elementImpl, elementImpl.getSimpleName().toString());
                            cacheOrDefault.add(classFullName);
                            cache.put(typeMirror.getQualifiedName().toString(), cacheOrDefault);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void generateConfigFiles() {
        try{
            for (String spiInterface : cache.keySet()) {
                Filer filer = processingEnv.getFiler();
                Writer writer = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + spiInterface).openWriter();
                for (String s : cache.get(spiInterface)) {
                    writer.write(s);
                    writer.write("\n");
                }
                writer.close();
            }
        }catch (Exception e){
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    /**
     * 考虑spi实现为匿名内部类
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