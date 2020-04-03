package schemabuilder.annotations.graphql

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class GraphQLDataFetcher(
        /**
         * The field name to be associated with the DataFetcher.
         * If none is provided the name of the method is used.
         *
         * @return
         */
        val value: String = "",
        val cost: Int = 1
)