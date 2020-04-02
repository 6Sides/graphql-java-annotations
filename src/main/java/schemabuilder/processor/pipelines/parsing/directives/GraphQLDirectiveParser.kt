package schemabuilder.processor.pipelines.parsing.directives

import graphql.schema.idl.SchemaDirectiveWiring
import schemabuilder.annotations.graphql.GraphQLDirective
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.wiring.InstanceFetcher

class GraphQLDirectiveParser : GraphQLClassParserStrategy {

    override fun parse(clazz: Class<*>, fetcher: InstanceFetcher) {
        val typeName: String = clazz.getAnnotation(GraphQLDirective::class.java).value
        val instance = fetcher.getInstance(clazz) as? SchemaDirectiveWiring ?: return
        GraphQLDirectiveBank.addDirective(GraphQLDirectiveType(typeName, instance))
    }

    override fun shouldParse(clazz: Class<*>): Boolean = clazz.isAnnotationPresent(GraphQLDirective::class.java)
}