package schemabuilder.processor.wiring

import com.google.common.collect.ImmutableSet
import com.google.common.reflect.ClassPath
import java.io.IOException
import java.util.*

class PackageScanner(private val basePackage: String?) {
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @return The classes
     * @throws IOException
     */
    @get:Throws(IOException::class)
    val classes: MutableList<Class<*>>
        get() {
            val classLoader = Thread.currentThread().contextClassLoader!!
            val classes = ArrayList<Class<*>>()
            val cp = ClassPath.from(Thread.currentThread().contextClassLoader)
            val scannedClasses: ImmutableSet<ClassPath.ClassInfo>

            scannedClasses = if (basePackage == null || basePackage == "") {
                cp.allClasses
            } else {
                cp.getTopLevelClassesRecursive(basePackage)
            }

            for (info in scannedClasses) {
                try {
                    classes.add(info.load())
                } catch (ignored: Throwable) {
                }
            }
            return classes
        }

}