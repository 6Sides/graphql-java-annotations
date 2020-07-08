package schemabuilder.processor.pipelines.parsing.federation

import schemabuilder.annotations.graphql.federation.GraphQLType
import schemabuilder.annotations.graphql.federation.TypeLoader
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy
import schemabuilder.processor.pipelines.parsing.ParsedResults
import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class FederationParser : GraphQLClassParserStrategy {

    override fun parse(clazz: KClass<*>, fetcher: InstanceFetcher) {
        val anno = clazz.findAnnotation<GraphQLType>()!!
        ParsedResults.types[clazz] = FederationData(
                anno.name,
                anno.idField,
                anno.loader.createInstance() as TypeLoader<*>
        )
    }

    @ExperimentalStdlibApi
    override fun shouldParse(clazz: KClass<*>) = clazz.hasAnnotation<GraphQLType>()
}

data class FederationData(
    val typename: String,
    val idField: String,
    val loader: TypeLoader<*>
)