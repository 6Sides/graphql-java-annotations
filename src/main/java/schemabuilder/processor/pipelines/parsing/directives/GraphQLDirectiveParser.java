package schemabuilder.processor.pipelines.parsing.directives;

import graphql.schema.idl.SchemaDirectiveWiring;
import schemabuilder.annotations.graphql.GraphQLDirective;
import schemabuilder.processor.pipelines.parsing.GraphQLClassParserStrategy;
import schemabuilder.processor.wiring.InstanceFetcher;

public class GraphQLDirectiveParser implements GraphQLClassParserStrategy {

    private GraphQLDirectiveBank directiveBank = GraphQLDirectiveBank.getInstance();

    @Override
    public void parse(Class<?> clazz, InstanceFetcher fetcher) {
        if (!clazz.isAnnotationPresent(GraphQLDirective.class)) {
            return;
        }

        String typeName = clazz.getAnnotation(GraphQLDirective.class).value();
        Object instance = fetcher.getInstance(clazz);

        if (!(instance instanceof SchemaDirectiveWiring)) {
            return;
        }

        directiveBank.addDirective(new GraphQLDirectiveType(typeName, (SchemaDirectiveWiring) instance));
    }
}
