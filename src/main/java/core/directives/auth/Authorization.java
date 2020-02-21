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
 * 2. Implement the {@link PermissionCheck} interface in your GraphQL Context object.
 */
@GraphQLDirective("auth")
public class Authorization implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> schemaDirectiveWiringEnv) {
        String targetAuthRole = (String) schemaDirectiveWiringEnv.getDirective().getArgument("permission").getValue();
        GraphQLFieldDefinition field = schemaDirectiveWiringEnv.getElement();

        // Build a data fetcher that first checks authorization roles before then calling the original data fetcher
        DataFetcher originalDataFetcher = field.getDataFetcher();

        DataFetcher authDataFetcher = dataFetchingEnvironment -> {
            PermissionCheck ctx = dataFetchingEnvironment.getContext();

            if (ctx.hasPermission(targetAuthRole)) {
                return originalDataFetcher.get(dataFetchingEnvironment);
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("error", new AuthorizationFailedResponse());
                return result;
            }
        };

        // Now change the field definition to have the new authorizing data fetcher
        return field.transform(builder -> builder.dataFetcher(authDataFetcher));
    }
}
