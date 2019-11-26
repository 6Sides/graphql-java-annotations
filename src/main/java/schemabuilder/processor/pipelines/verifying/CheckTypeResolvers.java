package schemabuilder.processor.pipelines.verifying;

import graphql.schema.TypeResolver;
import java.util.List;
import java.util.Map;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.wiring.MappingContainer;

public class CheckTypeResolvers extends GraphQLWiringVerificationStage {

    @Override
    protected void handle(ParsedGraphQLData data) {
        StringBuilder sb = new StringBuilder();
        boolean foundIssue = false;
        Map<String, List<MappingContainer<TypeResolver>>> typeResolvers = data.typeResolvers;

        for(String typeName : typeResolvers.keySet()) {
            List<MappingContainer<TypeResolver>> resolvers = typeResolvers.get(typeName);

            if(resolvers.size() > 1) {
                foundIssue = true;

                sb.append("Multiple definitions of the *** ").append(typeName)
                        .append(" *** type resolver:\n");

                for(MappingContainer<TypeResolver> container : resolvers) {
                    sb.append("\t").append(container.clazz.getTypeName()).append("\n");
                }
            }
        }

        if(foundIssue) {
            issues += sb.toString();
        }

        handleNext(data);
    }
}
