package schemabuilder.processor.pipelines.parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherBank;
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLTypeParser;
import schemabuilder.processor.pipelines.parsing.dataloaders.DataLoaderRepository;
import schemabuilder.processor.pipelines.parsing.dataloaders.GraphQLDataLoaderParser;
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
    private InstanceFetcher instanceFetcher;

    private String basePackage;
    private Set<Class<?>> additionalClasses;


    public GraphQLClassParser(String basePackage, Set<Class<?>> additionalClasses, InstanceFetcher fetcher) {
        this.basePackage = basePackage;
        this.additionalClasses = additionalClasses;
        this.instanceFetcher = fetcher;

        strategies.add(new GraphQLTypeParser());
        strategies.add(new GraphQLDirectiveParser());
        strategies.add(new GraphQLScalarParser());
        strategies.add(new GraphQLTypeResolverParser());
        strategies.add(new GraphQLDataLoaderParser());
    }

    public GraphQLClassParser() {
        this(null, new HashSet<>(), new DefaultInstanceFetcher());
    }

    public void parseClasses() {
        try {
            List<Class<?>> classes = new PackageScanner(this.basePackage).getClasses();
            classes.addAll(this.additionalClasses);
            classes = classes.stream().distinct().collect(Collectors.toList());

            for (Class<?> clazz : classes) {
                if (clazz.isInterface() || clazz.isAnnotation()) {
                    continue;
                }

                for (GraphQLClassParserStrategy strategy : strategies) {
                    strategy.parse(clazz, this.instanceFetcher);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Printing DataFetchers");
        GraphQLDataFetcherBank dataFetchers = GraphQLDataFetcherBank.getInstance();
        dataFetchers.getDataFetchers().forEach(System.out::println);

        System.out.println("Printing Directives");
        GraphQLDirectiveBank directives = GraphQLDirectiveBank.getInstance();
        directives.getDirectives().forEach(System.out::println);

        System.out.println("Printing Scalars");
        GraphQLScalarBank scalars = GraphQLScalarBank.getInstance();
        scalars.getScalars().forEach(System.out::println);

        System.out.println("Printing Type Resolvers");
        GraphQLTypeResolverBank typeResolvers = GraphQLTypeResolverBank.getInstance();
        typeResolvers.getTypeResolvers().forEach(System.out::println);

        System.out.println("Printing Batch Loaders");
        DataLoaderRepository dataLoaders = DataLoaderRepository.getInstance();
        dataLoaders.getDataLoaderRegistry().getDataLoaders().forEach(System.out::println);
    }

}
