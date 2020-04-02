package schemabuilder.processor.pipelines.parsing.datafetchers

import graphql.schema.DataFetcher
import schemabuilder.annotations.graphql.GraphQLDataFetcher
import schemabuilder.annotations.graphql.GraphQLTypeConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.wiring.InstanceFetcher
import java.lang.reflect.InvocationTargetException

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

            try {
                val inst = method.invoke(instance) as DataFetcher<*>
                DataFetcherCostMap.setCostFor(inst, cost)
                GraphQLDataFetcherBank.addDataFetcher(GraphQLDataFetcherType(typeName, cost, fieldName, inst))
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }

    override fun shouldParse(clazz: Class<*>): Boolean = clazz.isAnnotationPresent(GraphQLTypeConfiguration::class.java)
}