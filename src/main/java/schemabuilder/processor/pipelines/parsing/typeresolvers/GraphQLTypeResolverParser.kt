package schemabuilder.processor.pipelines.parsing.typeresolvers

import graphql.schema.TypeResolver
import schemabuilder.annotations.graphql.GraphQLTypeResolver
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class GraphQLTypeResolverParser : GraphQLClassParserStrategy {

    override fun parse(clazz: KClass<*>, fetcher: InstanceFetcher) {
        val typeName: String = clazz.findAnnotation<GraphQLTypeResolver>()?.value!!

        val instance = fetcher.getInstance(clazz)

        ParsedResults.typeResolvers.add(GraphQLTypeResolverType(typeName, instance as TypeResolver))
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLTypeResolver>()
}