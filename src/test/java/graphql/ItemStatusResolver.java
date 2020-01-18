package graphql;

import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import schemabuilder.annotations.graphql.GraphQLTypeResolver;

@GraphQLTypeResolver("ItemStatus")
public class ItemStatusResolver implements TypeResolver {

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object javaObject = env.getObject();

        return null;
    }
}