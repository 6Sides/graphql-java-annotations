package schemabuilder.processor.pipelines.parsing.datafetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import schemabuilder.annotations.graphql.GraphQLDataFetcher
import schemabuilder.annotations.graphql.GraphQLTypeConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf

class GraphQLTypeParser : GraphQLClassParserStrategy {

    @ExperimentalStdlibApi
    override fun parse(clazz: KClass<*>, fetcher: InstanceFetcher) {
        val typeName: String = clazz.findAnnotation<GraphQLTypeConfiguration>()?.value!!

        for (method in clazz.declaredFunctions) {
            val annotation = method.findAnnotation<GraphQLDataFetcher>()!!

            val fieldName = if (annotation.value == "") {
                method.name
            } else {
                annotation.value
            }

            val cost: Int = annotation.cost
            method.isAccessible = true

            try {
                val dataFetcherInstance = DataFetcher { environment ->
                    val paramNames = method.parameters.map { Pair(it.name, it.type) }.filter { it.first != null }

                    val params: MutableList<Any?> = paramNames.map { (first, second) ->
                        return@map if (second.isSubtypeOf(typeOf<DataFetchingEnvironment>())) {
                            environment
                        } else {
                            environment.arguments[first]
                        }
                    }.toMutableList()

                    params.add(0, fetcher.getInstance(clazz))

                    method.call(*params.toTypedArray())
                }

                DataFetcherCostMap.setCostFor(dataFetcherInstance, cost)
                ParsedResults.datafetchers.add(GraphQLDataFetcherType(typeName, cost, fieldName, dataFetcherInstance))
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLTypeConfiguration>()
}