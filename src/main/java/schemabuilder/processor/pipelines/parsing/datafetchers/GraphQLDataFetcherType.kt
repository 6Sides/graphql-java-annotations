package schemabuilder.processor.pipelines.parsing.datafetchers

import graphql.schema.DataFetcher

data class GraphQLDataFetcherType(val typeName: String, val cost: Int, val name: String, val dataFetcher: DataFetcher<*>)