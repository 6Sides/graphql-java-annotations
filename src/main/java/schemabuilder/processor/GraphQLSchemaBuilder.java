package schemabuilder.processor;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import schemabuilder.processor.pipelines.building.WiringBuilder;
import schemabuilder.processor.schema.SchemaParser;

/**
 * A class that builds {@link GraphQLSchema} objects
 */
public class GraphQLSchemaBuilder {

    private final WiringBuilder builder;
    private final SchemaParser schemaParser;

    /**
     * Creates A GraphQLSchemaBuilder object used to generate a {@link GraphQLSchema}
     *
     * @param schemaDirectory The directory to search for graphql schema files. The directory must be in the
     *                        resources folder.
     * @param schemaFileExtension The file type to look for. Only files ending in the provided
     *                            extension will be merged into the schema. Note that the extension
     *                            should not contain a "." E.g. "graphqls", "graphql", etc.
     * @param basePackage The base package to recursively search for GraphQL Annotations.
     */
    public GraphQLSchemaBuilder(String schemaDirectory, String schemaFileExtension, String basePackage) {
        this(
                new SchemaParser(schemaDirectory, schemaFileExtension)
        );
    }

    /**
     * Creates A GraphQLSchemaBuilder object used to generate a {@link GraphQLSchema}
     *
     * @param schemaParser The {@link SchemaParser} to use.
     * @param builder The {@link GraphQLWiringBuilder} to use.
     */
    public GraphQLSchemaBuilder(final SchemaParser schemaParser) {
        this.schemaParser = schemaParser;
        this.builder = new WiringBuilder();
    }

    /*public void addClass(Class<?> clazz) {
        this.builder.addClass(clazz);
    }*/

    /**
     * TODO: Make exceptions more explicit and well defined
     *
     * Generate and return a GraphQL Schema.
     *
     * @return A valid {@link GraphQLSchema} object
     * @throws Exception If the schema cannot be built
     */
    public GraphQLSchema getSchema() throws Exception {
        TypeDefinitionRegistry typeRegistry = schemaParser.getRegistry();
        RuntimeWiring runtimeWiring = builder.buildWiring().build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }
}
