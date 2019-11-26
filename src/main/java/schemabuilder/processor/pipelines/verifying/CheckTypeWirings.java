package schemabuilder.processor.pipelines.verifying;

import graphql.schema.DataFetcher;
import java.util.List;
import java.util.Map;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.wiring.MappingContainer;

public class CheckTypeWirings extends GraphQLWiringVerificationStage {

    @Override
    protected void handle(ParsedGraphQLData data) {
        StringBuilder sb = new StringBuilder();
        boolean foundIssue = false;
        Map<String, Map<String, List<MappingContainer<DataFetcher<?>>>>> types = data.types;

        for(String typeName : types.keySet()) {
            Map<String, List<MappingContainer<DataFetcher<?>>>> fieldFetchers = types.get(typeName);

            for(String fieldName : fieldFetchers.keySet()) {
                List<MappingContainer<DataFetcher<?>>> fetchers = fieldFetchers.get(fieldName);

                if(fetchers.size() > 1) {
                    foundIssue = true;

                    String path = typeName + "->" + fieldName;
                    sb.append("Multiple definitions of the *** ").append(path)
                            .append(" *** data fetcher:\n");

                    for(MappingContainer<DataFetcher<?>> container : fetchers) {
                        sb.append("\t").append(container.clazz.getTypeName()).append("#")
                                .append(container.methodName).append("\n");
                    }
                }
            }
        }

        if(foundIssue) {
            issues += sb.toString();
        }

        handleNext(data);
    }
}
