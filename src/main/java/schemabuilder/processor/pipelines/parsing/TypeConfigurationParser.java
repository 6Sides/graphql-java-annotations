package schemabuilder.processor.pipelines.parsing;

import graphql.schema.DataFetcher;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import schemabuilder.annotations.GraphQLDataFetcher;
import schemabuilder.annotations.GraphQLTypeConfiguration;
import schemabuilder.processor.wiring.MappingContainer;

public class TypeConfigurationParser extends GraphQLWiringParserStage {

    @Override
    public void handle(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GraphQLTypeConfiguration.class)) {
            handleNext(clazz);
            return;
        }

        String typeName = clazz.getAnnotation(GraphQLTypeConfiguration.class).value();
        if (typeName.equals("")) {
            typeName = clazz.getSimpleName();
        }

        Map<String, List<MappingContainer<DataFetcher<?>>>> fieldDataFetchers = parsedResults.types.get(typeName);
        if (fieldDataFetchers == null) {
            fieldDataFetchers = new HashMap<>();
        }

        Object instance = fetcher.getInstance(clazz);

        for (Method m : clazz.getDeclaredMethods()) {
            GraphQLDataFetcher annotation = m.getAnnotation(GraphQLDataFetcher.class);
            if(annotation == null || !m.getReturnType().equals(DataFetcher.class)) {
                continue;
            }

            String fieldName;
            if(annotation.value().equals("")) {
                fieldName = m.getName();
            } else {
                fieldName = annotation.value();
            }

            m.setAccessible(true);
            DataFetcher<?> fetcher;
            try {
                fetcher = (DataFetcher<?>) m.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                continue;
            }

            MappingContainer<DataFetcher<?>> container = new MappingContainer<>();
            container.clazz = clazz;
            container.obj = fetcher;
            container.methodName = m.getName();

            if(fieldDataFetchers.containsKey(fieldName)) {
                fieldDataFetchers.get(fieldName).add(container);
            } else {
                List<MappingContainer<DataFetcher<?>>> fetcherList = new ArrayList<>();
                fetcherList.add(container);
                fieldDataFetchers.put(fieldName, fetcherList);
            }
        }

        parsedResults.types.put(typeName, fieldDataFetchers);

        handleNext(clazz);
    }
}
