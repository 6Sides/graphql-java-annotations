package schemabuilder.processor.pipelines.parsing.typeresolvers;

import graphql.schema.TypeResolver;
import schemabuilder.annotations.graphql.GraphQLTypeResolver;
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy;
import schemabuilder.processor.wiring.InstanceFetcher;

public class GraphQLTypeResolverParser implements GraphQLClassParserStrategy {

    private GraphQLTypeResolverBank typeResolverBank = GraphQLTypeResolverBank.getInstance();

    @Override
    public void parse(Class<?> clazz, InstanceFetcher fetcher) {
        if (!clazz.isAnnotationPresent(GraphQLTypeResolver.class)) {
            return;
        }

        String typeName = clazz.getAnnotation(GraphQLTypeResolver.class).value();
        Object instance = fetcher.getInstance(clazz);

        typeResolverBank.addTypeResolver(new GraphQLTypeResolverType(typeName, (TypeResolver) instance));
    }
}
