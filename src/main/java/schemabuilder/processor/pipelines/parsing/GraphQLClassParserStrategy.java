package schemabuilder.processor.pipelines.parsing;

import schemabuilder.processor.wiring.InstanceFetcher;

public interface GraphQLClassParserStrategy {

    void parse(Class<?> clazz, InstanceFetcher fetcher);

}
