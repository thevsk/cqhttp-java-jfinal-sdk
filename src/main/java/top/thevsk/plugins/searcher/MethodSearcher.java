package top.thevsk.plugins.searcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class MethodSearcher {
    public static void addMethod(Class<?> clazz, Class annotation, Set<Method> list) {
        Method[] methods = clazz.getMethods();
        F1:
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation an : annotations) {
                if (an.annotationType().equals(annotation)) {
                    list.add(method);
                    continue F1;
                }
            }
        }
    }
}