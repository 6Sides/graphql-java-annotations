package schemabuilder.processor.wiring;

import graphql.schema.idl.RuntimeWiring;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import schemabuilder.processor.pipelines.building.BuildDirectives;
import schemabuilder.processor.pipelines.building.BuildScalars;
import schemabuilder.processor.pipelines.building.BuildTypeWirings;
import schemabuilder.processor.pipelines.building.GraphQLWiringBuilderStage;
import schemabuilder.processor.pipelines.parsing.DirectiveParser;
import schemabuilder.processor.pipelines.parsing.FilterInterfaces;
import schemabuilder.processor.pipelines.parsing.GraphQLWiringParserStage;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.pipelines.parsing.ScalarParser;
import schemabuilder.processor.pipelines.parsing.TypeConfigurationParser;
import schemabuilder.processor.pipelines.parsing.TypeResolverParser;
import schemabuilder.processor.pipelines.verifying.CheckDirectives;
import schemabuilder.processor.pipelines.verifying.CheckScalars;
import schemabuilder.processor.pipelines.verifying.CheckTypeResolvers;
import schemabuilder.processor.pipelines.verifying.CheckTypeWirings;
import schemabuilder.processor.pipelines.verifying.GraphQLWiringVerificationStage;

/**
 * A class that parses a specified directory and generates a {@link RuntimeWiring} for a graphql schema
 */
public class GraphQLWiringBuilder {

    /**
     * Since state can't change during runtime, lazily load the builder.
     */
    private RuntimeWiring.Builder builder = null;

    private final String basePackage;
    private final InstanceFetcher fetcher;
    private final boolean shouldPrintHierarchy;

    /**
     * Create a GraphQLWiringBuilder with given a package name. Uses the default options.
     * @param packageName
     */
    public GraphQLWiringBuilder(String packageName) {
        this(
                new GraphQLWiringBuilderOptions.Builder()
                        .basePackage(packageName)
                        .build()
        );
    }

    /**
     * Create a GraphQLWiringBuilder with options
     * @param options
     */
    public GraphQLWiringBuilder(GraphQLWiringBuilderOptions options) {
        this.basePackage = options.getBasePackage();
        this.fetcher = options.getInstanceFetcher();
        this.shouldPrintHierarchy = options.shouldPrintHierarchy();
    }

    /**
     * Retrieve a {@link RuntimeWiring.Builder} object
     * @return
     * @throws Exception
     */
    private RuntimeWiring.Builder getRuntimeWiringBuilder() throws Exception {
        if (builder != null) {
            return builder;
        }

        PackageScanner scanner = new PackageScanner(basePackage);

        GraphQLWiringParserStage head = new FilterInterfaces();
        head.linkWith(new TypeConfigurationParser())
                .linkWith(new DirectiveParser())
                .linkWith(new TypeResolverParser())
                .linkWith(new ScalarParser());

        List<Class<?>> clazzes = scanner.getClasses();
        clazzes.addAll(this.clazzes);
        clazzes = clazzes.stream().distinct().collect(Collectors.toList());

        ParsedGraphQLData data = head.kickoff(clazzes, fetcher);

        GraphQLWiringVerificationStage headVerify = new CheckDirectives();
        headVerify.linkWith(new CheckScalars())
                .linkWith(new CheckTypeResolvers())
                .linkWith(new CheckTypeWirings());

        headVerify.kickoff(data);

        GraphQLWiringBuilderStage headBuilder = new BuildTypeWirings();
        headBuilder.linkWith(new BuildScalars())
                .linkWith(new BuildDirectives());

        return headBuilder.kickoff(data, this.shouldPrintHierarchy);
    }

    private List<Class<?>> clazzes = new ArrayList<>();
    public void addClass(Class<?> clazz) {
        clazzes.add(clazz);
    }

    /**
     * Retrieve a {@link RuntimeWiring} object
     * @return
     * @throws Exception
     */
    public RuntimeWiring getRuntimeWiring() throws Exception {
        if(builder == null) {
            builder = this.getRuntimeWiringBuilder();
        }

        return builder.build();
    }
}
