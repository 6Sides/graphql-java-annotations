package schemabuilder.processor.pipelines.building;

import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring.Builder;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.wiring.MappingContainer;

public class BuildScalars extends GraphQLWiringBuilderStage {

    @Override
    protected Builder handle(ParsedGraphQLData data) {
        for(MappingContainer<GraphQLScalarType> container : data.scalars) {
            builder.scalar(container.obj);
        }

        return handleNext(data);
    }
}
