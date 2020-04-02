package schemabuilder.processor.pipelines.parsing.scalars

import schemabuilder.annotations.graphql.GraphQLScalar
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.wiring.InstanceFetcher
import java.lang.reflect.InvocationTargetException

class GraphQLScalarParser : GraphQLClassParserStrategy {
    private val scalarBank: GraphQLScalarBank = GraphQLScalarBank
    override fun parse(clazz: Class<*>, fetcher: InstanceFetcher) {
        val instance = fetcher.getInstance(clazz)
        for (method in clazz.declaredMethods) {
            val annotation = method.getAnnotation(GraphQLScalar::class.java)
            if (annotation == null || method.returnType != graphql.schema.GraphQLScalarType::class.java) {
                continue
            }
            method.isAccessible = true
            try {
                scalarBank.addScalar(GraphQLScalarType(annotation.value, method.invoke(instance) as graphql.schema.GraphQLScalarType))
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    override fun shouldParse(clazz: Class<*>): Boolean = clazz.isAnnotationPresent(GraphQLSchemaConfiguration::class.java)
}