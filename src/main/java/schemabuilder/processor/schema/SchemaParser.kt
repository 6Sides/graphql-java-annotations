package schemabuilder.processor.schema

import com.google.common.reflect.ClassPath
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import java.io.IOException
import java.util.*

/** Default graphql schema file extension to search for **/
const val defaultSchemaFileExtension = ".graphqls"

/**
 * A class that parses a directory for graphql schema definition
 * files and creates a valid [TypeDefinitionRegistry]
 */
class SchemaParser(private val schemaFileExtension: String = defaultSchemaFileExtension) {

    /**
     * Creates the TypeDefinitionRegistry from the .graphqls files in the schema directory
     *
     * @return A TypeDefinitionRegistry object constructed from the valid graphql schema definition files
     */
    @get:Throws(IOException::class)
    val registry: TypeDefinitionRegistry
        get() {
            return buildRegistryRecursively()
        }

    /**
     * Parses all resources in the classpath and merges all valid
     * file contents into the [TypeDefinitionRegistry]
     */
    @Throws(IOException::class)
    private fun buildRegistryRecursively(): TypeDefinitionRegistry {
        val registry = TypeDefinitionRegistry()
        val parser = SchemaParser()

        ClassPath.from(Thread.currentThread().contextClassLoader).resources.filter {
            it.resourceName.endsWith(schemaFileExtension)
        }.forEach { info ->
            readResourceContents(info).run {
                parser.parse(this)
            }.run {
                registry.merge(this)
            }
            println(info.resourceName)
        }

        return registry
    }

    private fun readResourceContents(resourceInfo: ClassPath.ResourceInfo): String {
        val s = Scanner(resourceInfo.asByteSource().openBufferedStream()).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}