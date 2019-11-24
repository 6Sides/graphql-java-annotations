package schemabuilder.processor.pipelines.building;

import graphql.schema.idl.RuntimeWiring.Builder;
import graphql.schema.idl.SchemaDirectiveWiring;
import java.util.List;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.wiring.MappingContainer;

public class BuildDirectives extends GraphQLWiringBuilderStage {

    @Override
    protected Builder handle(ParsedGraphQLData data) {
        for(String directiveName : data.directives.keySet()) {
            List<MappingContainer<SchemaDirectiveWiring>> directiveImpls = data.directives.get(directiveName);

            builder.directive(directiveName, directiveImpls.get(0).obj);
        }

        return handleNext(data);
    }
}
