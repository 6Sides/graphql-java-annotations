package schemabuilder.annotations.graphql.federation

import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class GraphQLType(
    val name: String,
    val idField: String = "id",
    val loader: KClass<*>
) {}

interface TypeLoader<T> {
    fun load(id: String): T?
}