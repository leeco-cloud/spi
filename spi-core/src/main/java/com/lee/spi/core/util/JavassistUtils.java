package com.lee.spi.core.util;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author yanhuai lee
 */
public class JavassistUtils {

    public static Class<?> generate(Class<?> interfaceType, String exceptionType) {
        try {
            if (!interfaceType.isInterface()) {
                throw new IllegalArgumentException("Only interfaces are supported");
            }

            ClassPool pool = ClassPool.getDefault();

            // 生成唯一的类名
            String className = interfaceType.getSimpleName() + "_Impl_" + System.currentTimeMillis();
            CtClass cc = pool.makeClass(className);

            // 实现接口
            cc.addInterface(pool.get(interfaceType.getName()));

            // 为每个抽象方法添加异常实现
            for (Method m : interfaceType.getMethods()) {
                int modifiers = m.getModifiers();

                // 只处理抽象方法（排除default、static和private方法）
                if (Modifier.isAbstract(modifiers)) {
                    CtMethod cm = new CtMethod(
                            pool.get(m.getReturnType().getName()),
                            m.getName(),
                            getParamTypes(pool, m),
                            cc
                    );

                    // 设置方法体：抛出指定类型的异常
                    String body = String.format(
                            "{ throw new %s(\"Method not implemented: %s\"); }",
                            exceptionType, m.getName()
                    );
                    cm.setBody(body);

                    cc.addMethod(cm);
                }
            }

            // 加载到JVM
            return cc.toClass();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static CtClass[] getParamTypes(ClassPool pool, Method method) throws NotFoundException {
        Class<?>[] paramTypes = method.getParameterTypes();
        CtClass[] ctParams = new CtClass[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            ctParams[i] = pool.get(paramTypes[i].getName());
        }
        return ctParams;
    }
}
