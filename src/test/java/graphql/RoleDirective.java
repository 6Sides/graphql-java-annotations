package graphql;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import schemabuilder.annotations.graphql.GraphQLDirective;

@GraphQLDirective("auth")
public class RoleDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(
            SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> schemaDirectiveWiringEnv) {
        String targetAuthRole = (String) schemaDirectiveWiringEnv.getDirective().getArgument("role").getValue();

        GraphQLFieldDefinition field = schemaDirectiveWiringEnv.getElement();
        //
        // build a data fetcher that first checks authorisation roles before then calling the original data fetcher
        //
        DataFetcher originalDataFetcher = field.getDataFetcher();
        DataFetcher authDataFetcher = dataFetchingEnvironment -> {
            return null;
        };
        //
        // now change the field definition to have the new authorising data fetcher
        return field.transform(builder -> builder.dataFetcher(authDataFetcher));
    }

}