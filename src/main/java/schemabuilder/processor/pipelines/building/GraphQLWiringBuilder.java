package schemabuilder.processor.pipelines.building;

import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;

public interface GraphQLWiringBuilder {

    void parse(ParsedGraphQLData data);

}
