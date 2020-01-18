package schemabuilder.processor;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import schemabuilder.processor.pipelines.building.WiringBuilder;
import schemabuilder.processor.schema.SchemaParser;

public final class GraphQLBuilder {

    private final WiringBuilder builder;
    private final SchemaParser schemaParser;


    private GraphQLBuilder(Set<Class<?>> additionalClasses, String basePackageForClasses, String schemaFileExtension) {
        this.builder = WiringBuilder.withOptions(basePackageForClasses, additionalClasses);
        this.schemaParser = new SchemaParser("", schemaFileExtension);
    }

    public GraphQL generateGraphQL() throws IOException {
        TypeDefinitionRegistry typeRegistry = schemaParser.getRegistry();
        RuntimeWiring runtimeWiring = builder.buildWiring().build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return GraphQL.newGraphQL(schema).build();
    }

    public static Builder newGraphQLBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Set<Class<?>> additionalClasses;
        private String basePackageForClasses;
        private String schemaFileExtension;

        public Builder() {
            this.additionalClasses = new HashSet<>();
            this.basePackageForClasses = null;
            this.schemaFileExtension = "graphqls";
        }

        public Builder addClass(Class<?> clazz) {
            this.additionalClasses.add(clazz);
            return this;
        }

        public Builder setBasePackageForClasses(String basePackage) {
            this.basePackageForClasses = basePackage;
            return this;
        }

        public Builder setSchemaFileExtension(String extension) {
            this.schemaFileExtension = extension;
            return this;
        }

        public GraphQLBuilder build() {
            return new GraphQLBuilder(
                    this.additionalClasses,
                    this.basePackageForClasses,
                    this.schemaFileExtension
            );
        }
    }
}
