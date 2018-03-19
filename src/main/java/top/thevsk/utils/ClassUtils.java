package top.thevsk.utils;

import com.jfinal.kit.StrKit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 类工具集
 *
 * @author L.cm
 */
public abstract class ClassUtils {

    /**
     * 获取当前线程
     *
     * @return 当前线程的class loader
     */
    public static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        return classLoader;
    }

    /**
     * 获得class loader，参考spring mvc
     *
     * @return 类加载器
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            // No thread context class loader -> use class loader of this class.
            classLoader = ClassUtils.class.getClassLoader();
            if (classLoader == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return classLoader;
    }

    /**
     * 加载类，参考SPring mvc
     *
     * @param className 类名
     * @return Class集合
     */
    public static Class<?> loadClass(String className) {
        if (className == null) {
            return null;
        }
        ClassLoader classLoader = ClassUtils.getClassLoader();
        try {
            return (classLoader != null ? classLoader.loadClass(className) : Class.forName(className));
        } catch (ClassNotFoundException e) {
            int lastDotIndex = className.lastIndexOf('.');
            if (lastDotIndex != -1) {
                String innerClassName = className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1);
                try {
                    return (classLoader != null ? classLoader.loadClass(innerClassName) : Class.forName(innerClassName));
                } catch (ClassNotFoundException ex2) {
                    // Swallow - let original exception get through
                }
            }
        }
        return null;
    }

    private static final String EXT_CLASS_LOADER_NAME = "sun.misc.Launcher$ExtClassLoader";

    /**
     * 本段代码来自jetbrick-template-1x
     * 根据 classLoader 获取所有的 Classpath URLs.
     */
    private static Collection<URL> getClasspathURLs(final ClassLoader classLoader) {
        Collection<URL> urls = new LinkedHashSet<URL>(32);
        ClassLoader loader = classLoader;
        while (loader != null) {
            String klassName = loader.getClass().getName();
            if (EXT_CLASS_LOADER_NAME.equals(klassName)) {
                break;
            }
            if (loader instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) loader).getURLs()) {
                    urls.add(url);
                }
            } else if (klassName.startsWith("weblogic.utils.classloaders.")) {
                // 该死的 WebLogic，只能特殊处理
                // GenericClassLoader, FilteringClassLoader, ChangeAwareClassLoader
                try {
                    Method method = loader.getClass().getMethod("getClassPath");
                    Object result = method.invoke(loader);
                    if (result != null) {
                        String[] paths = split(result.toString(), File.pathSeparatorChar);
                        for (String path : paths) {
                            urls.add(URLUtils.fromFile(path));
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            loader = loader.getParent();
        }

        String classpath = System.getProperty("java.class.path");
        if (classpath.length() > 1) {
            String[] paths = split(classpath, File.pathSeparatorChar);
            for (String path : paths) {
                path = path.trim();
                if (path.length() > 0) {
                    URL url = URLUtils.fromFile(path);
                    urls.add(url);
                }
            }
        }

        // 添加包含所有的 META-INF/MANIFEST.MF 的 jar 文件
        try {
            Enumeration<URL> paths = classLoader.getResources("META-INF/MANIFEST.MF");
            while (paths.hasMoreElements()) {
                URL url = paths.nextElement();
                File file = URLUtils.toFileObject(url);
                urls.add(file.toURI().toURL());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 删除 jdk 自带的 jar
        Iterator<URL> it = urls.iterator();
        while (it.hasNext()) {
            String path = it.next().getPath();
            if (path.contains("/jre/lib/")) {
                it.remove();
            }
        }

        return urls;
    }

    /**
     * 本段代码来自jetbrick-template-1x
     * 根据 classLoader 获取指定 package 对应的 URLs.
     */
    private static Collection<URL> getClasspathURLs(ClassLoader classLoader, String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("PackageName must be not null.");
        }
        Collection<URL> urls = new ArrayList<URL>();
        String dirname = packageName.replace('.', '/');
        try {
            Enumeration<URL> dirs = classLoader.getResources(dirname);
            while (dirs.hasMoreElements()) {
                urls.add(dirs.nextElement());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urls;
    }

    /**
     * 根据 packageName 获取指定 package 对应的 URLs.
     *
     * @param packageName 包名
     * @return URLs
     */
    public static Collection<URL> getClasspathURLs(String packageName) {
        ClassLoader classLoader = ClassUtils.getClassLoader();
        if (StrKit.isBlank(packageName)) {
            return ClassUtils.getClasspathURLs(classLoader);
        }
        return ClassUtils.getClasspathURLs(classLoader, packageName);
    }

    public static String[] split(String str, char delimiter) {
        List<String> results = new ArrayList<String>();

        int ipos = 0, lastpos = 0;
        while ((ipos = str.indexOf(delimiter, lastpos)) != -1) {
            results.add(str.substring(lastpos, ipos));
            lastpos = ipos + 1;
        }
        if (lastpos < str.length()) {
            results.add(str.substring(lastpos));
        }
        return results.toArray(new String[results.size()]);
    }
}