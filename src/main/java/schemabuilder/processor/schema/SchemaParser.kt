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
class SchemaParser(private val directory: String, schemaFileExtension: String?) {
    private val schemaFileExtension: String

    /**
     * Create a schema parser for the specified directory using
     * the [SchemaParser.defaultSchemaFileExtension] value.
     *
     * @param directory The directory to check for .graphqls files. Note that the
     * parser will only check for directories in the `resources` folder.
     */
    private constructor(directory: String) : this(directory, defaultSchemaFileExtension) {}

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
        val cp = ClassPath.from(Thread.currentThread().contextClassLoader)
        for (info in cp.resources) {
            try {
                if (info.resourceName.contains(schemaFileExtension)) {
                    println(info.resourceName)
                    val s = Scanner(info.asByteSource().openBufferedStream()).useDelimiter("\\A")
                    val fileContents = if (s.hasNext()) s.next() else ""
                    val contents = SchemaParser().parse(fileContents)
                    registry.merge(contents)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val defaultSchemaFileExtension = "graphqls"
    }

    /**
     * Create a schema parser for the specified directory.
     * @param directory The directory to check for .graphqls files. Note that the
     * parser will only check for directories in the `resources` folder.
     * @param schemaFileExtension The file type to look for. Only files ending in the provided
     * extension will be merged into the schema. Note that the extension
     * should not contain a "." E.g. "graphqls", "graphql", etc.
     */
    init {
        this.schemaFileExtension = String.format(".%s", schemaFileExtension)
    }
}