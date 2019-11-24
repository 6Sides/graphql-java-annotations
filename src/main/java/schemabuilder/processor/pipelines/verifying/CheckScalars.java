package schemabuilder.processor.pipelines.verifying;

import graphql.schema.GraphQLScalarType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.wiring.MappingContainer;

public class CheckScalars extends GraphQLWiringVerificationStage {

    @Override
    protected void handle(ParsedGraphQLData data) {
        StringBuilder sb = new StringBuilder();
        boolean foundIssue = false;
        List<MappingContainer<GraphQLScalarType>> scalars = data.scalars;

        Map<String, List<String>> scalarsClasses = new HashMap<>();

        for(MappingContainer<GraphQLScalarType> container : scalars) {
            String scalarName = container.obj.getName();

            if(scalarsClasses.containsKey(scalarName)) {
                scalarsClasses.get(scalarName).add(container.clazz.getName() + "#" + container.methodName);
            } else {
                List<String> classes = new ArrayList<>();
                classes.add(container.clazz.getName() + "#" + container.methodName);
                scalarsClasses.put(scalarName, classes);
            }
        }

        for(String scalarName : scalarsClasses.keySet()) {
            List<String> implementations = scalarsClasses.get(scalarName);

            if(implementations.size() > 1) {
                foundIssue = true;

                sb.append("Multiple definitions of the *** " + scalarName + " *** scalar type:\n");
                for(String className : implementations) {
                    sb.append("\t" + className + "\n");
                }
            }
        }

        if(foundIssue) {
            issues += sb.toString();
        }

        handleNext(data);
    }
}
