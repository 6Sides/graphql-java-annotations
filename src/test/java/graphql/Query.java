package graphql;

import graphql.schema.DataFetcher;
import schemabuilder.annotations.graphql.GraphQLDataFetcher;
import schemabuilder.annotations.graphql.GraphQLTypeConfiguration;

@GraphQLTypeConfiguration("Query")
public class Query {

    @GraphQLDataFetcher
    private DataFetcher test() {
        return env -> "Success!";
    }

}
