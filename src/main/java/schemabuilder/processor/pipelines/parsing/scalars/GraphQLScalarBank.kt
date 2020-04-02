package schemabuilder.processor.pipelines.parsing.scalars

object GraphQLScalarBank {
    fun addScalar(dataFetcher: GraphQLScalarType?) {
        scalars.add(dataFetcher)
    }

    val scalars: MutableSet<GraphQLScalarType?> = mutableSetOf()
}