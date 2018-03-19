package top.thevsk.plugins.searcher;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationReader {
    private Map<String, Class<? extends Annotation>> annotationMap = new HashMap();

    public AnnotationReader() {
    }

    public void addAnnotation(Class<? extends Annotation> annotation) {
        this.annotationMap.put('L' + annotation.getName().replace('.', '/') + ';', annotation);
    }

    public boolean isAnnotationed(InputStream inputStream) {
        ClassReader classReader;
        try {
            classReader = new ClassReader(inputStream);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }

        final boolean[] visitorResult = new boolean[]{false};
        classReader.accept(new ClassVisitor(327680) {
            public AnnotationVisitor visitAnnotation(String annotation, boolean visible) {
                if (AnnotationReader.this.annotationMap.containsKey(annotation)) {
                    visitorResult[0] = true;
                }

                return super.visitAnnotation(annotation, visible);
            }
        }, 1);
        return visitorResult[0];
    }
}
