package schemabuilder.processor.pipelines.parsing.scalars

import schemabuilder.annotations.graphql.GraphQLScalar
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class GraphQLScalarParser : GraphQLClassParserStrategy {

    override fun parse(clazz: KClass<*>, fetcher: InstanceFetcher) {
        for (method in clazz.declaredFunctions) {
            val annotation = method.findAnnotation<GraphQLScalar>()

            try {
                method.isAccessible = true
                ParsedResults.scalars.add(GraphQLScalarType(annotation?.value!!, method.call() as graphql.schema.GraphQLScalarType))

            } catch (e: Exception) {

            }
        }
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLSchemaConfiguration>()
}