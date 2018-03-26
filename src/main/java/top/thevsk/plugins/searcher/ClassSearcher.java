package top.thevsk.plugins.searcher;

import top.thevsk.utils.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClassSearcher {

    public ClassSearcher() {
    }

    public static Set<Class<?>> getClasses(String[] packageNames, Class... annotations) {
        final AnnotationReader reader = new AnnotationReader();
        Class[] var3 = annotations;
        int var4 = annotations.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            Class<? extends Annotation> annotation = var3[var5];
            reader.addAnnotation(annotation);
        }
        final Set<Class<?>> classes = new LinkedHashSet();
        FileSearcher finder = new FileSearcher() {
            public void visitFileEntry(FileEntry file) {
                if (file.isJavaClass()) {
                    try {
                        if (reader.isAnnotationed(file.getInputStream())) {
                            this.addClass(file.getQualifiedJavaName());
                        }
                    } catch (IOException var3) {
                        throw new RuntimeException(var3);
                    }
                }
            }
            private void addClass(String qualifiedClassName) {
                try {
                    Class<?> klass = ClassUtils.loadClass(qualifiedClassName);
                    classes.add(klass);
                } catch (Throwable var3) {
                    var3.printStackTrace();
                }
            }
        };
        finder.lookupClasspath(packageNames);
        return classes;
    }
}
