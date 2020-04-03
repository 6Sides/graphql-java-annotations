package schemabuilder.processor.pipelines.parsing.datafetchers

import graphql.schema.DataFetcher
import schemabuilder.annotations.graphql.GraphQLDataFetcher
import schemabuilder.annotations.graphql.GraphQLTypeConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import java.lang.Exception
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class GraphQLTypeParser : GraphQLClassParserStrategy {

    override fun parse(clazz: KClass<*>, fetcher: InstanceFetcher) {
        val typeName: String = clazz.findAnnotation<GraphQLTypeConfiguration>()?.value!!

        for (method in clazz.declaredFunctions) {
            val annotation = method.findAnnotation<GraphQLDataFetcher>()!!

            var fieldName: String

            fieldName = if (annotation.value == "") {
                method.name
            } else {
                annotation.value
            }
            val cost: Int = annotation.cost
            method.isAccessible = true

            try {
                val dataFetcherInstance = DataFetcher {
                    environment -> method.call(fetcher.getInstance(clazz), environment!!)
                }

                DataFetcherCostMap.setCostFor(dataFetcherInstance, cost)
                ParsedResults.datafetchers.add(GraphQLDataFetcherType(typeName, cost, fieldName, dataFetcherInstance))
            } catch (e: Exception) {e.printStackTrace()}
        }
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLTypeConfiguration>()
}