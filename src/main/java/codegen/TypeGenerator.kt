package codegen

import graphql.language.FieldDefinition
import schemabuilder.processor.schema.SchemaParser
import java.io.FileWriter

class TypeGenerator constructor(private val outputDir: String) {

    fun generateTypes() {
        val schemaParser = SchemaParser()
        val writer = FileWriter("Types")

        schemaParser.registry.types().forEach { (name, definition) ->
            println(name)

            definition.children.forEach {
                println(it::class)
                println((it as FieldDefinition).name)
            }
        }
    }

}