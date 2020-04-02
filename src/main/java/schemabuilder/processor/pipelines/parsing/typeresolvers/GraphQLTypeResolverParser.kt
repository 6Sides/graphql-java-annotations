package schemabuilder.processor.pipelines.parsing.typeresolvers

import graphql.schema.TypeResolver
import schemabuilder.annotations.graphql.GraphQLTypeResolver
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.wiring.InstanceFetcher

class GraphQLTypeResolverParser : GraphQLClassParserStrategy {

    override fun parse(clazz: Class<*>, fetcher: InstanceFetcher) {
        val typeName: String = clazz.getAnnotation(GraphQLTypeResolver::class.java).value
        val instance = fetcher.getInstance(clazz)
        GraphQLTypeResolverBank.addTypeResolver(GraphQLTypeResolverType(typeName, instance as TypeResolver))
    }

    override fun shouldParse(clazz: Class<*>): Boolean = clazz.isAnnotationPresent(GraphQLTypeResolver::class.java)
}