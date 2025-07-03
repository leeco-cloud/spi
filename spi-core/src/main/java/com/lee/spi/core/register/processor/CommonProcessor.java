package com.lee.spi.core.register.processor;

import com.alibaba.fastjson2.JSON;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yanhuai lee
 */
public abstract class CommonProcessor extends AbstractProcessor {

    private final List<Object> metaList = new CopyOnWriteArrayList<>();

    protected abstract String getSpiMetaInfoFilePath();

    protected abstract void processAnnotations(List<Object> metaInfo, Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
        } else {
            processAnnotations(metaList, annotations, roundEnv);
        }
        return true;
    }

    private void generateConfigFiles() {
        try{
            if (metaList.isEmpty()) {
                return;
            }
            Filer filer = processingEnv.getFiler();
            Writer writer = filer.createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    getSpiMetaInfoFilePath()
            ).openWriter();
            writer.write(JSON.toJSONString(metaList));
            writer.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

}
