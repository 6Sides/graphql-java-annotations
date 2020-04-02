package schemabuilder.processor.pipelines.parsing.typeresolvers

object GraphQLTypeResolverBank {

    fun addTypeResolver(typeResolver: GraphQLTypeResolverType?) {
        typeResolvers.add(typeResolver)
    }

    val typeResolvers: MutableSet<GraphQLTypeResolverType?> = mutableSetOf()

}