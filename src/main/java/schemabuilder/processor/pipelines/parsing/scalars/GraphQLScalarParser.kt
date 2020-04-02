package schemabuilder.processor.pipelines.parsing.scalars

import schemabuilder.annotations.graphql.GraphQLScalar
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class GraphQLScalarParser : GraphQLClassParserStrategy {
    override fun parse(clazz: Class<*>, fetcher: InstanceFetcher) {
        val instance = fetcher.getInstance(clazz)
        for (method in clazz.declaredMethods) {
            val annotation = method.getAnnotation(GraphQLScalar::class.java)
            if (annotation == null || method.returnType != graphql.schema.GraphQLScalarType::class.java) {
                continue
            }
            method.isAccessible = true

            ParsedResults.scalars.add(GraphQLScalarType(annotation.value, method.invoke(instance) as graphql.schema.GraphQLScalarType))
        }
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLSchemaConfiguration>()
}