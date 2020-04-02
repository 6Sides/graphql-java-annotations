package schemabuilder.processor.pipelines.parsing

import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherType
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveType
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarType
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverType

object ParsedResults {

    val datafetchers: MutableList<GraphQLDataFetcherType> = mutableListOf()
    val directives: MutableList<GraphQLDirectiveType> = mutableListOf()
    val scalars: MutableList<GraphQLScalarType> = mutableListOf()
    val typeResolvers: MutableList<GraphQLTypeResolverType> = mutableListOf()

}