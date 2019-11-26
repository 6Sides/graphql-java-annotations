package graphql;

import graphql.schema.DataFetcher;
import schemabuilder.annotations.GraphQLDataFetcher;
import schemabuilder.annotations.GraphQLTypeConfiguration;

@GraphQLTypeConfiguration("Query")
public class Query {

    @GraphQLDataFetcher
    private DataFetcher test() {
        return env -> "Success!";
    }

}
