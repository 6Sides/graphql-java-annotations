package schemabuilder.processor.pipelines.parsing.dataloaders

import org.dataloader.BatchLoader
import org.dataloader.MappedBatchLoader
import schemabuilder.annotations.graphql.GraphQLDataLoader
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class GraphQLDataLoaderParser : GraphQLClassParserStrategy {
    @ExperimentalStdlibApi
    override fun parse(clazz: KClass<*>, fetcher: InstanceFetcher) {

        for (method in clazz.declaredFunctions) {
            val annotation: GraphQLDataLoader = if (method.hasAnnotation<GraphQLDataLoader>()) {
                method.findAnnotation<GraphQLDataLoader>()!!
            } else {
                continue
            }

            method.isAccessible = true
            var fieldName: String

            if (annotation.value == "") {
                fieldName = method.name
            } else {
                fieldName = annotation.value
            }
            val returnType = method.returnType
            val inst = fetcher.getInstance(clazz)

            try {
                if (returnType == BatchLoader::class.java) {
                    DataLoaderRepository.addBatchLoader(fieldName, method.call(inst) as BatchLoader<*, *>)
                } else {
                    DataLoaderRepository.addBatchLoader(fieldName, method.call(inst) as MappedBatchLoader<*, *>)
                }
            } catch (e: Exception) {}
        }
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLSchemaConfiguration>()
}