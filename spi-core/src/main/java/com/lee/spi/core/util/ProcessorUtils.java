package com.lee.spi.core.util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yanhuai lee
 */
public class ProcessorUtils {

    private final Types typeUtils;
    private final Elements elementUtils;

    public ProcessorUtils(ProcessingEnvironment env) {
        this.typeUtils = env.getTypeUtils();
        this.elementUtils = env.getElementUtils();
    }

    /**
     * 递归获取类/接口的所有上层接口
     * @param typeElement 要分析的类/接口元素
     * @return 所有上层接口的TypeElement集合
     */
    public Set<TypeElement> getAllInterfaces(TypeElement typeElement) {
        Set<TypeElement> interfaces = new HashSet<>();
        collectInterfaces(typeElement, interfaces);
        return interfaces;
    }

    private void collectInterfaces(TypeElement typeElement, Set<TypeElement> results) {
        // 获取直接实现的接口
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            TypeElement interfaceElement = (TypeElement) typeUtils.asElement(interfaceType);

            if (interfaceElement != null && results.add(interfaceElement)) {
                // 递归收集父接口
                collectInterfaces(interfaceElement, results);
            }
        }

        // 处理父类（如果是类）
        TypeMirror superClass = typeElement.getSuperclass();
        TypeElement superClassElement = (TypeElement) typeUtils.asElement(superClass);

        // 排除java.lang.Object（没有接口）和未解析的类型
        if (superClassElement != null && !superClassElement.getQualifiedName().contentEquals("java.lang.Object")) {
            collectInterfaces(superClassElement, results);
        }
    }
}
