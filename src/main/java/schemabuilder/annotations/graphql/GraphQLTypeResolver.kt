package schemabuilder.annotations.graphql

@MustBeDocumented
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@GraphQLSchemaConfiguration
annotation class GraphQLTypeResolver(
        /**
         * The name of the type to associate the type resolver with
         *
         * @return
         */
        val value: String
)