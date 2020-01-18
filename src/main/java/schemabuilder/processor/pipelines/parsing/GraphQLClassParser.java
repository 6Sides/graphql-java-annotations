package schemabuilder.processor.pipelines.parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherBank;
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLTypeParser;
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveBank;
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveParser;
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarBank;
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarParser;
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverBank;
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverParser;
import schemabuilder.processor.wiring.DefaultInstanceFetcher;
import schemabuilder.processor.wiring.InstanceFetcher;
import schemabuilder.processor.wiring.PackageScanner;

public class GraphQLClassParser {

    private List<GraphQLClassParserStrategy> strategies = new ArrayList<>();
    private InstanceFetcher instanceFetcher = new DefaultInstanceFetcher();

    public GraphQLClassParser() {
        strategies.add(new GraphQLTypeParser());
        strategies.add(new GraphQLDirectiveParser());
        strategies.add(new GraphQLScalarParser());
        strategies.add(new GraphQLTypeResolverParser());
    }

    public void parseClasses() {
        try {
            for (Class<?> clazz : new PackageScanner("graphql").getClasses()) {
                for (GraphQLClassParserStrategy strategy : strategies) {
                    strategy.parse(clazz, this.instanceFetcher);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Printing dfs");
        GraphQLDataFetcherBank dataFetchers = GraphQLDataFetcherBank.getInstance();
        dataFetchers.getDataFetchers().forEach(System.out::println);

        System.out.println("Printing directives");
        GraphQLDirectiveBank directives = GraphQLDirectiveBank.getInstance();
        directives.getDirectives().forEach(System.out::println);

        System.out.println("Printing scalars");
        GraphQLScalarBank scalars = GraphQLScalarBank.getInstance();
        scalars.getScalars().forEach(System.out::println);

        System.out.println("Printing type resolvers");
        GraphQLTypeResolverBank typeResolvers = GraphQLTypeResolverBank.getInstance();
        typeResolvers.getTypeResolvers().forEach(System.out::println);
    }

}
