package schemabuilder.processor.wiring;

import com.google.common.reflect.ClassPath;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PackageScanner {

    private final String basePackage;

    PackageScanner(String packageName) {
        this.basePackage = packageName;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @return The classes
     * @throws IOException
     */
    final List<Class<?>> getClasses() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;


        ArrayList<Class<?>> classes = new ArrayList<>();

        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
        for(ClassPath.ClassInfo info : cp.getTopLevelClassesRecursive(this.basePackage)) {
            try {
                classes.add(info.load());
            } catch (Throwable e) {}
        }

        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    @Deprecated
    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                if(className.startsWith(".")) {
                    className = className.substring(1);
                }
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }
}
