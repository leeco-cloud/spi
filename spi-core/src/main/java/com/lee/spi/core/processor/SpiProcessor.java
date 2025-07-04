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
//                for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
//                    if (enclosedElement.getKind() == ElementKind.METHOD) {
//                        processMethod(spi, metaInfo, interfaceElement.getQualifiedName(), (ExecutableElement) enclosedElement);
//                    }
//                }

                SpiMeta spiMeta = new SpiMeta(interfaceElement.getQualifiedName().toString(), spi.code(), spi.name(), spi.desc(), spi.priority());
                spiMetas.add(spiMeta);
//                metaInfo.add(spiMeta);

            }
            // 处理加在方法上的注解
//            else if (element.getKind() == ElementKind.METHOD) {
//                // 当前方法
//                ExecutableElement executableElement = (ExecutableElement) element;
//                // 方法所在接口
//                Element interfaceElement = executableElement.getEnclosingElement();
//                if (interfaceElement.getKind() == ElementKind.INTERFACE && interfaceElement instanceof TypeElement) {
//                    TypeElement interfaceTypeElement = (TypeElement) (executableElement.getEnclosingElement());
//                    processMethod(spi, metaInfo, interfaceTypeElement.getQualifiedName(), executableElement);
//                }
//            }
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

//    private void processMethod(Spi spi, List<Object> metaInfo, Name interfaceName, ExecutableElement methodElement) {
//        // 获取方法名
//        String methodName = methodElement.getSimpleName().toString();
//
//        // 获取入参类型列表
//        List<String> paramTypes = new ArrayList<>();
//        for (VariableElement param : methodElement.getParameters()) {
//            TypeMirror paramType = param.asType();
//            paramTypes.add(paramType.toString()); // 获取参数类型的字符串表示
//        }
//
//        SpiMeta spiMeta = new SpiMeta(interfaceName.toString(), methodName, paramTypes, spi.code(), spi.name(), spi.desc(), spi.priority());
//
//        metaInfo.add(spiMeta);
//    }

}