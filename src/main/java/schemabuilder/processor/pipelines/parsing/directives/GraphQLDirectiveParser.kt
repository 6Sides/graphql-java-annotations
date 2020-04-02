package schemabuilder.processor.pipelines.parsing.directives

import graphql.schema.idl.SchemaDirectiveWiring
import schemabuilder.annotations.graphql.GraphQLDirective
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class GraphQLDirectiveParser : GraphQLClassParserStrategy {

    override fun parse(clazz: Class<*>, fetcher: InstanceFetcher) {
        val typeName: String = clazz.getAnnotation(GraphQLDirective::class.java).value
        val instance = fetcher.getInstance(clazz) as? SchemaDirectiveWiring ?: return
        ParsedResults.directives.add(GraphQLDirectiveType(typeName, instance))
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLDirective>()
}