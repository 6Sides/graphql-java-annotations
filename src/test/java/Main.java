import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import schemabuilder.processor.GraphQLSchemaBuilder;

public class Main {

    public static void main(String[] args) throws Exception {
        GraphQLSchema schema = new GraphQLSchemaBuilder("graphql_schema", "graphqls", "graphql").getSchema();

        GraphQL gql = GraphQL.newGraphQL(schema).build();

        System.out.println(gql.execute("query { test }"));
    }
}
