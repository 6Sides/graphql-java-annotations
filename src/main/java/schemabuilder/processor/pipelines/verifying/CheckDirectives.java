package schemabuilder.processor.pipelines.verifying;

import graphql.schema.idl.SchemaDirectiveWiring;
import java.util.List;
import java.util.Map;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.wiring.MappingContainer;

public class CheckDirectives extends GraphQLWiringVerificationStage {

    @Override
    protected void handle(ParsedGraphQLData data) {
        StringBuilder sb = new StringBuilder();
        boolean foundIssue = false;
        Map<String, List<MappingContainer<SchemaDirectiveWiring>>> directives = data.directives;

        for(String directiveName : directives.keySet()) {
            List<MappingContainer<SchemaDirectiveWiring>> directiveImpls = directives.get(directiveName);

            if(directiveImpls.size() > 1) {
                foundIssue = true;

                sb.append("Multiple definitions of the *** " + directiveName + " *** directive:\n");

                for(MappingContainer<SchemaDirectiveWiring> container : directiveImpls) {
                    sb.append("\t" + container.clazz.getTypeName() + "\n");
                }
            }
        }

        if(foundIssue) {
            issues += sb.toString();
        }

        handleNext(data);
    }
}
