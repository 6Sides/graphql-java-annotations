package schemabuilder.processor.pipelines.parsing.dataloaders;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.dataloader.BatchLoader;
import org.dataloader.MappedBatchLoader;
import schemabuilder.annotations.graphql.GraphQLDataLoader;
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration;
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy;
import schemabuilder.processor.wiring.InstanceFetcher;

public class GraphQLDataLoaderParser implements GraphQLClassParserStrategy {

    @Override
    public void parse(Class<?> clazz, InstanceFetcher fetcher) {
        if (!clazz.isAnnotationPresent(GraphQLSchemaConfiguration.class)) {
            return;
        }

        Object instance = fetcher.getInstance(clazz);

        for (Method method : clazz.getDeclaredMethods()) {
            GraphQLDataLoader annotation = method.getAnnotation(GraphQLDataLoader.class);
            if(annotation == null || !method.getReturnType().equals(BatchLoader.class)
                    && !method.getReturnType().equals(MappedBatchLoader.class)) {
                continue;
            }

            method.setAccessible(true);

            String fieldName;
            if(annotation.value().equals("")) {
                fieldName = method.getName();
            } else {
                fieldName = annotation.value();
            }

            Class<?> returnType = method.getReturnType();

            try {
                if (returnType.equals(BatchLoader.class)) {
                    DataLoaderRepository.getInstance().addBatchLoader(fieldName, (BatchLoader<?,?>) method.invoke(instance));
                } else {
                    DataLoaderRepository.getInstance().addBatchLoader(fieldName, (MappedBatchLoader<?,?>) method.invoke(instance));
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
