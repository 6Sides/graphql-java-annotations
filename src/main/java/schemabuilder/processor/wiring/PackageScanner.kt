package schemabuilder.processor.wiring

import com.google.common.collect.ImmutableSet
import com.google.common.reflect.ClassPath
import java.io.IOException
import java.util.*

class PackageScanner(private val basePackage: String?) {

    /**
     * Scans all classes accessible from the context class loader which
     * belong to the given package and subpackages.
     *
     * @return The classes in the base package
     * @throws IOException
     */
    @get:Throws(IOException::class)
    val classes: List<Class<*>>
        get() {
            val classPath = ClassPath.from(Thread.currentThread().contextClassLoader)
            val scannedClasses: ImmutableSet<ClassPath.ClassInfo>

            scannedClasses = if (basePackage.isNullOrBlank()) {
                classPath.allClasses
            } else {
                classPath.getTopLevelClassesRecursive(basePackage)
            }

            return scannedClasses.map {
                it.load()
            }
        }

}