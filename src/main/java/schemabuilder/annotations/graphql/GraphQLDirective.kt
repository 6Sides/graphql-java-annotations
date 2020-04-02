package schemabuilder.annotations.graphql

@MustBeDocumented
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@GraphQLSchemaConfiguration
annotation class GraphQLDirective(
        /**
         * The name of the GraphQL directive the class is representing.
         *
         * @return The name of the GraphQL directive to represent
         */
        val value: String)