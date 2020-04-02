package schemabuilder.processor.pipelines.building

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import schemabuilder.processor.pipelines.parsing.GraphQLClassParser
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherBank
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherType
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveBank
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarBank
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverBank
import schemabuilder.processor.wiring.InstanceFetcher
import java.util.*

class WiringBuilder private constructor(basePackage: String?, clazzes: Set<Class<*>?>, fetcher: InstanceFetcher) {

    fun buildWiring(): RuntimeWiring.Builder {
        val typeMap: MutableMap<String?, MutableSet<GraphQLDataFetcherType>> = HashMap()
        for (dataFetcher in GraphQLDataFetcherBank.dataFetchers) {
            typeMap.computeIfAbsent(dataFetcher.typeName) { HashSet() }
            typeMap[dataFetcher.typeName]?.add(dataFetcher)
        }
        val builder = RuntimeWiring.newRuntimeWiring()
        for (typeName in typeMap.keys) {
            val typeBuilder = TypeRuntimeWiring.newTypeWiring(typeName)
            println(typeName)
            for (dataFetcher in typeMap[typeName]!!) {
                typeBuilder.dataFetcher(dataFetcher.name, dataFetcher.dataFetcher)
                println("-" + dataFetcher.name)
            }
            builder.type(typeBuilder)
        }
        for (resolver in GraphQLTypeResolverBank.typeResolvers) {
            val typeResolverBuilder = TypeRuntimeWiring.newTypeWiring(resolver?.name)
            typeResolverBuilder.typeResolver(resolver?.resolver)
            builder.type(typeResolverBuilder)
        }
        for (scalar in GraphQLScalarBank.scalars) {
            builder.scalar(scalar?.scalar)
        }
        for (directive in GraphQLDirectiveBank.directives) {
            builder.directive(directive?.name, directive?.directive)
        }
        return builder
    }

    companion object {
        fun withOptions(basePackage: String?, clazzes: Set<Class<*>?>, fetcher: InstanceFetcher): WiringBuilder {
            return WiringBuilder(basePackage, clazzes, fetcher)
        }
    }

    init {
        GraphQLClassParser(basePackage, clazzes, fetcher).parseClasses()
    }
}