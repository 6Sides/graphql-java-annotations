package schemabuilder.processor.pipelines.building

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import schemabuilder.processor.pipelines.parsing.GraphQLClassParser
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherType
import schemabuilder.processor.wiring.InstanceFetcher
import java.util.*

class WiringBuilder private constructor(basePackage: String?, clazzes: Set<Class<*>>, fetcher: InstanceFetcher) {

    fun buildWiring(): RuntimeWiring.Builder {
        val typeMap: MutableMap<String?, MutableSet<GraphQLDataFetcherType>> = HashMap()
        for (dataFetcher in ParsedResults.datafetchers) {
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

        for (resolver in ParsedResults.typeResolvers) {
            val typeResolverBuilder = TypeRuntimeWiring.newTypeWiring(resolver.name)
            typeResolverBuilder.typeResolver(resolver.resolver)
            builder.type(typeResolverBuilder)
        }

        for (scalar in ParsedResults.scalars) {
            builder.scalar(scalar.scalar)
        }

        for (directive in ParsedResults.directives) {
            builder.directive(directive.name, directive.directive)
        }
        return builder
    }

    companion object {
        fun withOptions(basePackage: String?, clazzes: Set<Class<*>>, fetcher: InstanceFetcher): WiringBuilder {
            return WiringBuilder(basePackage, clazzes, fetcher)
        }
    }

    init {
        GraphQLClassParser(basePackage, clazzes, fetcher).parseClasses()
    }
}