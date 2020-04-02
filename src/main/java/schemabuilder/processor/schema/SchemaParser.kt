package schemabuilder.processor.schema

import com.google.common.reflect.ClassPath
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import java.io.File
import java.io.IOException
import java.util.*

/**
 * A class that parses a directory for graphql schema definition
 * files and creates a valid [TypeDefinitionRegistry]
 */
class SchemaParser(directory: String, schemaFileExtension: String?) {
    private val schemaFileExtension: String = ".${schemaFileExtension ?: defaultSchemaFileExtension}"

    /**
     * Create a schema parser for the specified directory using
     * the [schemaFileExtension] value.
     *
     * @param directory The directory to check for .graphqls files. Note that the
     * parser will only check for directories in the `resources` folder.
     */


    /**
     * Creates the TypeDefinitionRegistry from the .graphqls files in the schema directory
     *
     * @return A TypeDefinitionRegistry object constructed from the valid graphql schema definition files
     */
    @get:Throws(IOException::class)
    val registry: TypeDefinitionRegistry
        get() {
            val typeRegistry = TypeDefinitionRegistry()
            buildRegistryRecursively(typeRegistry, null)
            return typeRegistry
        }

    /**
     * Parses the specified directory and merges all valid file contents into the [TypeDefinitionRegistry]
     *
     * @param registry The context to merge .graphqls file schemas into
     * @param directory The root directory to search through
     */
    @Throws(IOException::class)
    private fun buildRegistryRecursively(registry: TypeDefinitionRegistry, directory: File?) {
        val classPath = ClassPath.from(Thread.currentThread().contextClassLoader)
        val parser = SchemaParser()

        classPath.resources.filter {
            it.resourceName.endsWith(schemaFileExtension)
        }.forEach { info ->
            readFileContents(info).run {
                parser.parse(this)
            }.run {
                registry.merge(this)
            }
            println(info.resourceName)
        }
    }

    private fun readFileContents(resourceInfo: ClassPath.ResourceInfo): String {
        val s = Scanner(resourceInfo.asByteSource().openBufferedStream()).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    companion object {
        private const val defaultSchemaFileExtension = "graphqls"
    }

}