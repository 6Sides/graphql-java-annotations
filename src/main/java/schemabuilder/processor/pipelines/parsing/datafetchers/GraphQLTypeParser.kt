package schemabuilder.processor.pipelines.parsing.datafetchers

import graphql.schema.DataFetcher
import schemabuilder.annotations.graphql.GraphQLDataFetcher
import schemabuilder.annotations.graphql.GraphQLTypeConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class GraphQLTypeParser : GraphQLClassParserStrategy {

    override fun parse(clazz: Class<*>, fetcher: InstanceFetcher) {
        val typeName: String = clazz.getAnnotation(GraphQLTypeConfiguration::class.java).value

        val instance = fetcher.getInstance(clazz)
        for (method in clazz.declaredMethods) {
            val annotation = method.getAnnotation(GraphQLDataFetcher::class.java)
            if (annotation == null || method.returnType != DataFetcher::class.java) {
                continue
            }
            var fieldName: String

            fieldName = if (annotation.value == "") {
                method.name
            } else {
                annotation.value
            }
            val cost: Int = annotation.cost
            method.isAccessible = true

            val inst = method.invoke(instance) as DataFetcher<*>
            DataFetcherCostMap.setCostFor(inst, cost)
            ParsedResults.datafetchers.add(GraphQLDataFetcherType(typeName, cost, fieldName, inst))
        }
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLTypeConfiguration>()
}