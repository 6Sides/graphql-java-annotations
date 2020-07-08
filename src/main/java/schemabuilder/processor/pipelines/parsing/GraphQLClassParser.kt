package schemabuilder.processor.pipelines.parsing

import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLTypeParser
import schemabuilder.processor.pipelines.parsing.dataloaders.DataLoaderRepository
import schemabuilder.processor.pipelines.parsing.dataloaders.GraphQLDataLoaderParser
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveParser
import schemabuilder.processor.pipelines.parsing.federation.FederationParser
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarParser
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverParser
import schemabuilder.processor.wiring.DefaultInstanceFetcher
import schemabuilder.processor.wiring.InstanceFetcher
import schemabuilder.processor.wiring.PackageScanner
import java.util.*

class GraphQLClassParser @JvmOverloads constructor(
        private val basePackage: String? = null,
        private val additionalClasses: Set<Class<*>> = HashSet(),
        private val instanceFetcher: InstanceFetcher = DefaultInstanceFetcher()
) {

    private val strategies: List<GraphQLClassParserStrategy> = listOf(
            GraphQLTypeParser(),
            GraphQLDirectiveParser(),
            GraphQLScalarParser(),
            GraphQLTypeResolverParser(),
            GraphQLDataLoaderParser(),
            FederationParser()
    )

    fun parseClasses() {
        PackageScanner(basePackage).classes.union(additionalClasses).filter {
            !(it.isInterface || it.isAnnotation)
        }.forEach { clazz ->
            strategies.filter {
                it.shouldParse(clazz.kotlin)
            }.forEach {
                it.parse(clazz.kotlin, instanceFetcher)
            }
        }


        println("Printing DataFetchers")
        ParsedResults.datafetchers.forEach(::println)
        println("Printing Directives")
        ParsedResults.directives.forEach(::println)
        println("Printing Scalars")
        ParsedResults.scalars.forEach(::println)
        println("Printing Type Resolvers")
        ParsedResults.typeResolvers.forEach(::println)
        println("Printing Batch Loaders")
        DataLoaderRepository.dataLoaderRegistry.dataLoaders.forEach(::println)
        println("Printing Batch Loaders")
        ParsedResults.types.forEach { (k, v) -> println("$v: $k") }
    }
}