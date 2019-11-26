package schemabuilder.processor.pipelines.parsing;

import graphql.schema.GraphQLScalarType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import schemabuilder.annotations.GraphQLScalar;
import schemabuilder.processor.wiring.MappingContainer;

public class ScalarParser extends GraphQLWiringParserStage {

    @Override
    public void handle(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GraphQLScalar.class)) {
            return;
        }

        Object instance = fetcher.getInstance(clazz);

        for(Method method : clazz.getDeclaredMethods()) {
            GraphQLScalar annotation = method.getAnnotation(GraphQLScalar.class);
            if(annotation == null || !method.getReturnType().equals(GraphQLScalarType.class)) {
                continue;
            }

            method.setAccessible(true);

            MappingContainer<GraphQLScalarType> container = new MappingContainer<>();
            container.clazz = clazz;
            try {
                container.obj = (GraphQLScalarType) method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                continue;
            }
            container.methodName = method.getName();

            parsedResults.scalars.add(container);
        }

        handleNext(clazz);
    }
}
