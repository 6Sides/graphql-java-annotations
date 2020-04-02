package core.directives.auth;

import com.google.inject.Inject;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.util.HashMap;
import java.util.Map;
import schemabuilder.annotations.graphql.GraphQLDirective;
import schemabuilder.processor.pipelines.parsing.datafetchers.DataFetcherCostMap;

/**
 * To use this directive:
 *
 * 1. Include `directive @auth(policy: Int!) on FIELD_DEFINITION` in a .graphqls file.
 * 2. Implement the {@link PolicyChecker} interface in your GraphQL Context object.
 *
 * <T> The type of the context
 */
//@GraphQLDirective("auth")
public class Authorization implements SchemaDirectiveWiring {

    private final PolicyCheckProvider provider;

    @Inject
    public Authorization(PolicyCheckProvider provider) {
        this.provider = provider;
    }

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> schemaDirectiveWiringEnv) {
        String targetAuthRole = (String) schemaDirectiveWiringEnv.getDirective().getArgument("policy").getValue();
        GraphQLFieldDefinition field = schemaDirectiveWiringEnv.getElement();

        // Build a data fetcher that first checks authorization roles before then calling the original data fetcher
        DataFetcher originalDataFetcher = field.getDataFetcher();

        DataFetcher authDataFetcher = dataFetchingEnvironment -> {
            Object ctx = dataFetchingEnvironment.getContext();

            Object result = provider.create().hasPermission(ctx, targetAuthRole);

            if (result == null) {
                return originalDataFetcher.get(dataFetchingEnvironment);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", result);
                return response;
            }
        };

        DataFetcherCostMap.setCostFor(authDataFetcher, DataFetcherCostMap.getCostFor(originalDataFetcher));

        // Now change the field definition to have the new authorizing data fetcher
        return field.transform(builder -> builder.dataFetcher(authDataFetcher));
    }
}
