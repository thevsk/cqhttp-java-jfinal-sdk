package top.thevsk.plugins.searcher;

import top.thevsk.utils.ClassUtils;
import top.thevsk.utils.URLUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileSearcher {
    public FileSearcher() {
    }

    public void lookupClasspath(String... packageNames) {
        if (packageNames == null || packageNames.length == 0) {
            Collection<URL> urls = ClassUtils.getClasspathURLs(null);
            doGetClasspathResources(urls, null);
        } else {
            for (String pkg : packageNames) {
                Collection<URL> urls = ClassUtils.getClasspathURLs(pkg);
                doGetClasspathResources(urls, pkg.replace('.', '/'));
            }
        }
    }

    private void doGetClasspathResources(Collection<URL> urls, String pkgdir) {
        for (URL url : urls) {
            String protocol = url.getProtocol();
            System.out.println(protocol + " \t " + url);
            if ("file".equals(protocol)) {
                File file = URLUtils.toFileObject(url);
                if (file.isDirectory()) {
                    doLookupInFileSystem(file, pkgdir, null);
                } else {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".jar") || name.endsWith(".zip")) {
                        doLookupInZipFile(url, pkgdir);
                    }
                }
            } else if ("jar".equals(protocol) || "zip".equals(protocol)) {
                doLookupInZipFile(url, pkgdir);
            } else if ("vfs".equals(protocol)) {
                doLookupInVfsFile(url, pkgdir);
            } else {
                throw new IllegalStateException("Unsupported url format: "
                        + url.toString());
            }
        }
    }

    private void doLookupInFileSystem(File dir, String pkgdir, String relativeName) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String name = (relativeName == null) ? file.getName() : relativeName + '/' + file.getName();
            SystemFileEntry entry = new SystemFileEntry(file, pkgdir, name);
            if (file.isDirectory()) {
                if (visitSystemDirEntry(entry)) {
                    doLookupInFileSystem(file, pkgdir, name);
                }
            } else {
                visitSystemFileEntry(entry);
            }
        }
    }

    private void doLookupInZipFile(URL url, String pkgdir) {
        Object zip = null;
        try {
            if ("jar".equals(url.getProtocol())) {
                zip = ((JarURLConnection) url.openConnection()).getJarFile();
            } else {
                File file = URLUtils.toFileObject(url);
                if (!file.exists()) {
                    return;
                }
                zip = new ZipFile(file);
            }
            this.doLookupInZipFile((ZipFile) zip, pkgdir);
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        } finally {
            try {
                if (zip != null) {
                    ((Closeable) zip).close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void doLookupInZipFile(ZipFile zip, String pkgdir) {
        if (pkgdir != null && pkgdir.length() != 0) {
            pkgdir = pkgdir + '/';
        } else {
            pkgdir = null;
        }

        Enumeration entries = zip.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String entryName = entry.getName();
            if (entry.isDirectory()) {
                entryName = entryName.substring(0, entryName.length() - 1);
            }

            if (pkgdir == null) {
                if (entry.isDirectory()) {
                    this.visitZipDirEntry(new FileSearcher.ZipFileEntry(zip, entry, entryName));
                } else {
                    this.visitZipFileEntry(new FileSearcher.ZipFileEntry(zip, entry, entryName));
                }
            } else if (entryName.startsWith(pkgdir)) {
                entryName = entryName.substring(pkgdir.length());
                if (entry.isDirectory()) {
                    this.visitZipDirEntry(new FileSearcher.ZipFileEntry(zip, entry, entryName));
                } else {
                    this.visitZipFileEntry(new FileSearcher.ZipFileEntry(zip, entry, entryName));
                }
            }
        }
    }

    private void doLookupInVfsFile(URL url, String pkgdir) {
        try {
            URLConnection conn = url.openConnection();
            if (conn.getClass().getName().equals("org.jboss.vfs.protocol.VirtualFileURLConnection")) {
                String vfs = conn.getContent().toString();
                File file = new File(vfs.substring(1, vfs.length() - 1));
                if (file.exists()) {
                    if (file.isDirectory()) {
                        this.doLookupInFileSystem(file, pkgdir, (String) null);
                    } else {
                        String name = file.getName().toLowerCase();
                        if (name.endsWith(".jar") || name.endsWith(".zip")) {
                            ZipFile zip = new ZipFile(file);

                            try {
                                this.doLookupInZipFile(zip, pkgdir);
                            } finally {
                                try {
                                    if (zip != null) {
                                        zip.close();
                                    }
                                } catch (IOException e) {
                                }
                            }
                        }
                    }

                }
            } else {
                throw new IllegalStateException("Unsupported URL: " + url);
            }
        } catch (Exception var12) {
            throw new RuntimeException(var12);
        }
    }

    protected boolean visitSystemDirEntry(FileSearcher.SystemFileEntry dir) {
        return this.visitDirEntry(dir);
    }

    protected void visitSystemFileEntry(FileSearcher.SystemFileEntry file) {
        this.visitFileEntry(file);
    }

    protected void visitZipDirEntry(FileSearcher.ZipFileEntry dir) {
        this.visitDirEntry(dir);
    }

    protected void visitZipFileEntry(FileSearcher.ZipFileEntry file) {
        this.visitFileEntry(file);
    }

    protected boolean visitDirEntry(FileSearcher.FileEntry dir) {
        return true;
    }

    protected void visitFileEntry(FileSearcher.FileEntry file) {
    }

    public static class ZipFileEntry implements FileSearcher.FileEntry {
        private final ZipFile zip;
        private final ZipEntry entry;
        private final String relativeName;

        public ZipFileEntry(ZipFile zip, ZipEntry entry, String relativeName) {
            this.zip = zip;
            this.entry = entry;
            this.relativeName = relativeName;
        }

        public ZipFile getZipFile() {
            return this.zip;
        }

        public ZipEntry getZipEntry() {
            return this.entry;
        }

        public boolean isDirectory() {
            return this.entry.isDirectory();
        }

        public boolean isJavaClass() {
            return this.entry.getName().endsWith(".class");
        }

        public String getName() {
            int ipos = this.relativeName.lastIndexOf(47);
            return ipos != -1 ? this.relativeName.substring(ipos + 1) : this.relativeName;
        }

        public String getRelativePathName() {
            return this.relativeName;
        }

        public String getQualifiedFileName() {
            String name = this.entry.getName();
            return this.entry.isDirectory() ? name.substring(0, name.length() - 1).replace('/', '.') : name;
        }

        public String getQualifiedJavaName() {
            String name = this.getQualifiedFileName();
            if (this.entry.isDirectory()) {
                return name;
            } else if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace('/', '.');
            } else {
                throw new IllegalStateException("FileEntry is not a Java Class: " + this.toString());
            }
        }

        public long length() {
            return this.entry.getSize();
        }

        public long lastModified() {
            return this.entry.getTime();
        }

        public InputStream getInputStream() throws IOException {
            return this.zip.getInputStream(this.entry);
        }

        public String toString() {
            return this.entry.toString();
        }
    }

    public static class SystemFileEntry implements FileSearcher.FileEntry {
        private final File file;
        private final String pkgdir;
        private final String relativeName;

        public SystemFileEntry(File file, String pkgdir, String relativeName) {
            this.file = file;
            this.pkgdir = pkgdir;
            this.relativeName = relativeName;
        }

        public File getFile() {
            return this.file;
        }

        public boolean isDirectory() {
            return this.file.isDirectory();
        }

        public boolean isJavaClass() {
            return !this.file.isDirectory() && this.file.getName().endsWith(".class");
        }

        public String getName() {
            return this.file.getName();
        }

        public String getRelativePathName() {
            return this.relativeName;
        }

        public String getQualifiedFileName() {
            String name;
            if (this.pkgdir != null) {
                name = this.pkgdir + '/' + this.relativeName;
            } else {
                name = this.relativeName;
            }

            return this.file.isDirectory() ? name.replace('/', '.') : name;
        }

        public String getQualifiedJavaName() {
            String name = this.getQualifiedFileName();
            if (this.file.isDirectory()) {
                return name;
            } else if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace('/', '.');
            } else {
                throw new IllegalStateException("FileEntry is not a Java Class: " + this.toString());
            }
        }

        public long length() {
            return this.file.length();
        }

        public long lastModified() {
            return this.file.lastModified();
        }

        public InputStream getInputStream() throws IOException {
            return new FileInputStream(this.file);
        }

        public String toString() {
            return this.file.toString();
        }
    }

    public interface FileEntry {
        boolean isDirectory();

        boolean isJavaClass();

        String getName();

        String getRelativePathName();

        String getQualifiedFileName();

        String getQualifiedJavaName();

        long length();

        long lastModified();

        InputStream getInputStream() throws IOException;
    }
}
