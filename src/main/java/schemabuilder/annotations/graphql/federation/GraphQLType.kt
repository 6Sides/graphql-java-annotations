package schemabuilder.annotations.graphql.federation

import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class GraphQLType(
    val name: String,
    val idField: String = "id",
    val loader: KClass<*>
) {}

interface TypeLoader<I, R> {
    fun load(id: I): R?
}