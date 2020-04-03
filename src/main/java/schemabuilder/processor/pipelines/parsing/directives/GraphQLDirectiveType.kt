package schemabuilder.processor.pipelines.parsing.directives

import graphql.schema.idl.SchemaDirectiveWiring

data class GraphQLDirectiveType(val name: String, val directive: SchemaDirectiveWiring)