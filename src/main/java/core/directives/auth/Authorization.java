package core.directives.auth;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.util.HashMap;
import java.util.Map;
import schemabuilder.annotations.graphql.GraphQLDirective;

/**
 * To use this directive:
 *
 * 1. Include `directive @auth(permission : String!) on FIELD_DEFINITION` in a .graphqls file.
 * 2. Implement the {@link PolicyCheck} interface in your GraphQL Context object.
 */
@GraphQLDirective("auth")
public class Authorization implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> schemaDirectiveWiringEnv) {
        Integer targetAuthRole = (Integer) schemaDirectiveWiringEnv.getDirective().getArgument("policyId").getValue();
        GraphQLFieldDefinition field = schemaDirectiveWiringEnv.getElement();

        // Build a data fetcher that first checks authorization roles before then calling the original data fetcher
        DataFetcher originalDataFetcher = field.getDataFetcher();

        DataFetcher authDataFetcher = dataFetchingEnvironment -> {
            PolicyCheck ctx = dataFetchingEnvironment.getContext();

            Object result = ctx.hasPermission(targetAuthRole);
            if (result == null) {
                return originalDataFetcher.get(dataFetchingEnvironment);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", result);
                return response;
            }
        };

        // Now change the field definition to have the new authorizing data fetcher
        return field.transform(builder -> builder.dataFetcher(authDataFetcher));
    }
}
