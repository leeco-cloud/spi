package com.lee.spi.core.register.processor;

import com.lee.spi.core.util.ServicesFileUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanhuai lee
 */
public abstract class CommonProcessor extends AbstractProcessor {

    public abstract Class<? extends Annotation> spiAnnotation();
    private final Map<String, String> providers = new ConcurrentHashMap<>();

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return Set.of(spiAnnotation().getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_11;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
        } else {
            processAnnotations(annotations, roundEnv);
        }
        return true;
    }

    private void generateConfigFiles() {
        Filer filer = processingEnv.getFiler();

        for (String spiAnnotation : providers.keySet()) {
            String resourceFile = "META-INF/services/" + spiAnnotation;
            try {
                SortedSet<String> allServices = new TreeSet<>();
                try {
                    FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                    Set<String> oldServices = ServicesFileUtils.readServiceFile(existingFile.openInputStream());
                    allServices.addAll(oldServices);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                Set<String> newServices = new HashSet<>();
                newServices.add(providers.get(spiAnnotation));
                if (allServices.containsAll(newServices)) {
                    return;
                }

                allServices.addAll(newServices);

//                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, allServices.toString());

                FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                OutputStream out = fileObject.openOutputStream();
                ServicesFileUtils.writeServiceFile(allServices, out);
                out.close();
            } catch (IOException e) {
                return;
            }
        }
    }

    private void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(spiAnnotation());
        for (Element e : elements) {
            TypeElement providerImplementer = null;
            if (e instanceof TypeElement) {
                providerImplementer = (TypeElement) e;
            }
            if (e.getKind() == ElementKind.METHOD && e instanceof ExecutableElement) {
                ExecutableElement executableElement = (ExecutableElement) e;
                Element element = executableElement.getEnclosingElement();
                if (element.getKind() == ElementKind.CLASS && element instanceof TypeElement) {
                    providerImplementer = (TypeElement) (executableElement.getEnclosingElement());
                }
            }
            if (providerImplementer == null) {
                continue;
            }
            providers.put(spiAnnotation().getName(), getBinaryName(providerImplementer));
        }
    }

    /**
     * Returns the binary name create a reference type. For example,
     * {@code com.google.Foo$Bar}, instead create {@code com.google.Foo.Bar}.
     */
    private String getBinaryName(TypeElement element) {
        return getBinaryNameImpl(element, element.getSimpleName().toString());
    }

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
