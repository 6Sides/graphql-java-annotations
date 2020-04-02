package schemabuilder.annotations.graphql

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@GraphQLSchemaConfiguration
annotation class GraphQLDataLoader(
        /**
         * The field name to be associated with the DataLoader. If none is provided the name of the
         * method is used.
         *
         * @return
         */
        val value: String = "")