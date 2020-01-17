package schemabuilder.processor.pipelines.parsing;

import java.util.ArrayList;
import java.util.List;
import schemabuilder.processor.wiring.DefaultInstanceFetcher;
import schemabuilder.processor.wiring.InstanceFetcher;

public class GraphQLClassParser {

    private List<GraphQLClassParserStrategy> strategies = new ArrayList<>();

    private InstanceFetcher instanceFetcher = new DefaultInstanceFetcher();
    private ParsedGraphQLData parsedGraphQLData = new ParsedGraphQLData();

    public GraphQLClassParser() {

    }

    /*
    public ParsedGraphQLData parseClasses() {
        strategies.forEach();
    }*/

}
