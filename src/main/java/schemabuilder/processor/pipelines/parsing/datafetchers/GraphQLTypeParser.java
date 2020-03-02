package schemabuilder.processor.pipelines.parsing.datafetchers;

import graphql.schema.DataFetcher;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import schemabuilder.annotations.graphql.GraphQLDataFetcher;
import schemabuilder.annotations.graphql.GraphQLTypeConfiguration;
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy;
import schemabuilder.processor.wiring.InstanceFetcher;

public class GraphQLTypeParser implements GraphQLClassParserStrategy {

    private GraphQLDataFetcherBank dataFetcherBank = GraphQLDataFetcherBank.getInstance();

    @Override
    public void parse(Class<?> clazz, InstanceFetcher fetcher) {
        if (!clazz.isAnnotationPresent(GraphQLTypeConfiguration.class)) {
            return;
        }

        String typeName = clazz.getAnnotation(GraphQLTypeConfiguration.class).value();
        Object instance = fetcher.getInstance(clazz);


        for (Method method : clazz.getDeclaredMethods()) {
            GraphQLDataFetcher annotation = method.getAnnotation(GraphQLDataFetcher.class);
            if(annotation == null || !method.getReturnType().equals(DataFetcher.class)) {
                continue;
            }

            String fieldName;
            if(annotation.value().equals("")) {
                fieldName = method.getName();
            } else {
                fieldName = annotation.value();
            }

            int cost = annotation.cost();

            method.setAccessible(true);
            try {
                dataFetcherBank.addDataFetcher(new GraphQLDataFetcherType(typeName, cost, fieldName, (DataFetcher<?>) method.invoke(instance)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
