package schemabuilder.processor.pipelines.parsing;

import graphql.schema.idl.SchemaDirectiveWiring;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import schemabuilder.annotations.GraphQLDirective;
import schemabuilder.processor.wiring.MappingContainer;

public class DirectiveParser extends GraphQLWiringParserStage {

    @Override
    public void handle(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GraphQLDirective.class)) {
            return;
        }

        String typeName = clazz.getAnnotation(GraphQLDirective.class).value();
        Object instance = fetcher.getInstance(clazz);

        MappingContainer<SchemaDirectiveWiring> container = new MappingContainer<>();
        container.clazz = clazz;
        container.obj = (SchemaDirectiveWiring) instance;

        Map<String, List<MappingContainer<SchemaDirectiveWiring>>> directives = parsedResults.directives;
        if (directives.containsKey(typeName)) {
            directives.get(typeName).add(container);
        } else {
            List<MappingContainer<SchemaDirectiveWiring>> directiveList = new ArrayList<>();
            directiveList.add(container);
            directives.put(typeName, directiveList);
        }

        handleNext(clazz);
    }
}
