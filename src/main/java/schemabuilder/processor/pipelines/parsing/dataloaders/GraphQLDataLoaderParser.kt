package schemabuilder.processor.pipelines.parsing.dataloaders

import org.dataloader.BatchLoader
import org.dataloader.MappedBatchLoader
import schemabuilder.annotations.graphql.GraphQLDataLoader
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class GraphQLDataLoaderParser : GraphQLClassParserStrategy {
    override fun parse(clazz: Class<*>, fetcher: InstanceFetcher) {
        val instance = fetcher.getInstance(clazz)
        for (method in clazz.declaredMethods) {
            val annotation = method.getAnnotation(GraphQLDataLoader::class.java)
            if (annotation == null || method.returnType != BatchLoader::class.java
                    && method.returnType != MappedBatchLoader::class.java) {
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

            if (returnType == BatchLoader::class.java) {
                DataLoaderRepository.addBatchLoader(fieldName, method.invoke(instance) as BatchLoader<*, *>)
            } else {
                DataLoaderRepository.addBatchLoader(fieldName, method.invoke(instance) as MappedBatchLoader<*, *>)
            }
        }
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLSchemaConfiguration>()
}