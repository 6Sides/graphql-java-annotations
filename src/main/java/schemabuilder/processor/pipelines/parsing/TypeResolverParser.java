package schemabuilder.processor.pipelines.parsing;

import graphql.schema.TypeResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import schemabuilder.annotations.GraphQLTypeResolver;
import schemabuilder.processor.wiring.MappingContainer;

public class TypeResolverParser extends GraphQLWiringParserStage {

    @Override
    public void handle(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GraphQLTypeResolver.class)) {
            return;
        }

        System.out.println("Found type resolver!");

        String typeName = clazz.getAnnotation(GraphQLTypeResolver.class).value();

        System.out.println(typeName);

        Object instance = fetcher.getInstance(clazz);

        MappingContainer<TypeResolver> container = new MappingContainer<>();
        container.clazz = clazz;
        container.obj = (TypeResolver) instance;

        Map<String, List<MappingContainer<TypeResolver>>> typeResolvers = parsedResults.typeResolvers;
        if(typeResolvers.containsKey(typeName)) {
            typeResolvers.get(typeName).add(container);
        } else {
            List<MappingContainer<TypeResolver>> resolverList = new ArrayList<>();
            resolverList.add(container);
            typeResolvers.put(typeName, resolverList);
        }

        handleNext(clazz);
    }
}
