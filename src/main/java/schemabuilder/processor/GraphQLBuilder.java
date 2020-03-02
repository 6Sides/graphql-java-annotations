package schemabuilder.processor;

import graphql.GraphQL;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import schemabuilder.annotations.documentation.Stable;
import schemabuilder.processor.pipelines.building.WiringBuilder;
import schemabuilder.processor.schema.SchemaParser;

@Stable
public final class GraphQLBuilder {

    private static int maxQueryCost;

    private final WiringBuilder builder;
    private final SchemaParser schemaParser;
    private final ChainedInstrumentation instrumentation;

    private GraphQLBuilder(Set<Class<?>> additionalClasses, String basePackageForClasses, String schemaFileExtension, ChainedInstrumentation instrumentation, int maxQueryCost) {
        this.builder = WiringBuilder.withOptions(basePackageForClasses, additionalClasses);
        this.schemaParser = new SchemaParser("", schemaFileExtension);
        this.instrumentation = instrumentation;

        GraphQLBuilder.maxQueryCost = maxQueryCost;
    }

    public GraphQL generateGraphQL() throws IOException {
        TypeDefinitionRegistry typeRegistry = schemaParser.getRegistry();
        RuntimeWiring runtimeWiring = builder.buildWiring().build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return GraphQL.newGraphQL(schema)
                .instrumentation(instrumentation)
                .build();
    }

    public static Builder newGraphQLBuilder() {
        return new Builder();
    }

    public static int getMaxQueryCost() {
        return maxQueryCost;
    }

    public static class Builder {

        private Set<Class<?>> additionalClasses;
        private String basePackageForClasses;
        private String schemaFileExtension;
        private ChainedInstrumentation instrumentation;

        private Integer maxQueryCost = 1000;


        public Builder() {
            this.additionalClasses = new HashSet<>();
            this.basePackageForClasses = null;
            this.schemaFileExtension = "graphqls";


            List<Instrumentation> insts = new ArrayList<>();
            insts.add(
                    new DataLoaderDispatcherInstrumentation(
                            DataLoaderDispatcherInstrumentationOptions.newOptions()
                            .includeStatistics(true)
                    )
            );

            this.instrumentation = new ChainedInstrumentation(insts);
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

        public Builder setInstrumentaiton(ChainedInstrumentation instrumentation) {
            this.instrumentation = instrumentation;
            return this;
        }

        public Builder setMaxQueryCost(int maxQueryCost) {
            this.maxQueryCost = maxQueryCost;
            return this;
        }

        public GraphQLBuilder build() {
            return new GraphQLBuilder(
                    this.additionalClasses,
                    this.basePackageForClasses,
                    this.schemaFileExtension,
                    this.instrumentation,
                    this.maxQueryCost
            );
        }
    }
}
