package schemabuilder.processor.pipelines.parsing

import org.dataloader.DataLoader
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherBank
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherType
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLTypeParser
import schemabuilder.processor.pipelines.parsing.dataloaders.DataLoaderRepository
import schemabuilder.processor.pipelines.parsing.dataloaders.GraphQLDataLoaderParser
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveBank
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveParser
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveType
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarBank
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarParser
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarType
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverBank
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverParser
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverType
import schemabuilder.processor.wiring.DefaultInstanceFetcher
import schemabuilder.processor.wiring.InstanceFetcher
import schemabuilder.processor.wiring.PackageScanner
import java.io.IOException
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

class GraphQLClassParser @JvmOverloads constructor(private val basePackage: String? = null, private val additionalClasses: Set<Class<*>?> = HashSet(), private val instanceFetcher: InstanceFetcher = DefaultInstanceFetcher()) {
    private val strategies: MutableList<GraphQLClassParserStrategy> = ArrayList()
    fun parseClasses() {
        try {
            var classes = PackageScanner(basePackage).classes
            additionalClasses.forEach {
                classes.add(it!!)
            }
            classes = classes.stream().distinct().collect(Collectors.toList())
            for (clazz in classes) {
                if (clazz.isInterface || clazz.isAnnotation) {
                    continue
                }

                for (strategy in strategies) {
                    if (strategy.shouldParse(clazz)) {
                        strategy.parse(clazz, instanceFetcher)
                    }
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        println("Printing DataFetchers")
        GraphQLDataFetcherBank.dataFetchers.forEach(Consumer { x: GraphQLDataFetcherType? -> println(x) })
        println("Printing Directives")
        GraphQLDirectiveBank.directives.forEach(Consumer { x: GraphQLDirectiveType? -> println(x) })
        println("Printing Scalars")
        GraphQLScalarBank.scalars.forEach(Consumer { x: GraphQLScalarType? -> println(x) })
        println("Printing Type Resolvers")
        GraphQLTypeResolverBank.typeResolvers.forEach(Consumer { x: GraphQLTypeResolverType? -> println(x) })
        println("Printing Batch Loaders")
        DataLoaderRepository.dataLoaderRegistry.dataLoaders.forEach(Consumer { x: DataLoader<*, *>? -> println(x) })
    }

    init {
        strategies.add(GraphQLTypeParser())
        strategies.add(GraphQLDirectiveParser())
        strategies.add(GraphQLScalarParser())
        strategies.add(GraphQLTypeResolverParser())
        strategies.add(GraphQLDataLoaderParser())
    }
}