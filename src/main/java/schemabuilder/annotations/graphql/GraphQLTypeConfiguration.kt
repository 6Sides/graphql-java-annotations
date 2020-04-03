package schemabuilder.annotations.graphql

@MustBeDocumented
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@GraphQLSchemaConfiguration
annotation class GraphQLTypeConfiguration(
        /**
         * The graphql type name to associate datafetchers with.
         * @return
         */
        val value: String)