package com.lee.spi.spi.example;

import com.lee.spi.core.annotation.Identity;
import com.lee.spi.core.annotation.Spi;
import com.lee.spi.core.loader.AnnotationLoader;

import java.util.List;

/**
 * @author yanhuai lee
 */
@Identity(code = "aaa")
@Spi(code = "bbb")
public class IdentityTest2 {

    public static void main(String[] args) {
        List<AnnotationLoader.AnnotationData> annotatedClasses =
                AnnotationLoader.load("Identity");
        System.out.println("\nFound annotated classes:");
        annotatedClasses.forEach(System.out::println);

        // 实际使用场景：根据注解信息动态初始化组件
        annotatedClasses.forEach(data -> {
            System.out.println("Initializing: " + data.className);
            // 这里可以通过反射创建实例等操作
        });

        List<AnnotationLoader.AnnotationData> annotatedClasses2 =
                AnnotationLoader.load("Spi");
        System.out.println("\nFound annotated classes:");
        annotatedClasses2.forEach(System.out::println);

        // 实际使用场景：根据注解信息动态初始化组件
        annotatedClasses2.forEach(data -> {
            System.out.println("Initializing: " + data.className);
            // 这里可以通过反射创建实例等操作
        });
    }

}
