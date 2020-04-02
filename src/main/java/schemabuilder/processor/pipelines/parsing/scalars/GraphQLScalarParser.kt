package schemabuilder.processor.pipelines.parsing.scalars;

import graphql.schema.GraphQLScalarType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import schemabuilder.annotations.graphql.GraphQLScalar;
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration;
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy;
import schemabuilder.processor.wiring.InstanceFetcher;

public class GraphQLScalarParser implements GraphQLClassParserStrategy {

    private GraphQLScalarBank scalarBank = GraphQLScalarBank.getInstance();

    @Override
    public void parse(Class<?> clazz, InstanceFetcher fetcher) {
        if (!clazz.isAnnotationPresent(GraphQLSchemaConfiguration.class)) {
            return;
        }

        Object instance = fetcher.getInstance(clazz);

        for (Method method : clazz.getDeclaredMethods()) {
            GraphQLScalar annotation = method.getAnnotation(GraphQLScalar.class);
            if(annotation == null || !method.getReturnType().equals(graphql.schema.GraphQLScalarType.class)) {
                continue;
            }

            method.setAccessible(true);

            try {
                scalarBank.addScalar(new schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarType(annotation.value(), (GraphQLScalarType) method.invoke(instance)));
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
