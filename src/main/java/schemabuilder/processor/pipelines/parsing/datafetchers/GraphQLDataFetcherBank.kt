package schemabuilder.processor.pipelines.parsing.datafetchers

object GraphQLDataFetcherBank {
    fun addDataFetcher(dataFetcher: GraphQLDataFetcherType) {
        dataFetchers.add(dataFetcher)
    }

    val dataFetchers: MutableSet<GraphQLDataFetcherType> = mutableSetOf()
}