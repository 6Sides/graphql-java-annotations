package core.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import schemabuilder.annotations.graphql.GraphQLScalar;
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration;

@GraphQLSchemaConfiguration
public class UUID {

    @GraphQLScalar("UUID")
    public static GraphQLScalarType getUUIDScalar() {
        return GraphQLScalarType.newScalar()
                .name("UUID")
                .description("A custom scalar that handles UUID values")
                .coercing(
                        new Coercing() {
                            @Override
                            public Object serialize(Object dataFetcherResult)
                                    throws CoercingSerializeException {
                                if (!(dataFetcherResult instanceof java.util.UUID)) {
                                    throw new CoercingSerializeException(
                                            "Unable to serialize "
                                                    + dataFetcherResult
                                                    + " as a UUID");
                                }

                                return dataFetcherResult.toString();
                            }

                            @Override
                            public Object parseValue(Object input)
                                    throws CoercingParseValueException {
                                String possibleUUID = (String) input;
                                try {
                                    return java.util.UUID.fromString(possibleUUID);
                                } catch (IllegalArgumentException ex) {
                                    throw new CoercingParseValueException(ex.getMessage());
                                }
                            }

                            @Override
                            public Object parseLiteral(Object input)
                                    throws CoercingParseLiteralException {
                                if (input instanceof StringValue) {
                                    String possibleUUID = ((StringValue) input).getValue();

                                    try {
                                        return java.util.UUID.fromString(possibleUUID);
                                    } catch (IllegalArgumentException ex) {
                                        throw new CoercingParseLiteralException(ex.getMessage());
                                    }
                                }
                                throw new CoercingParseLiteralException(
                                        "Value is not a valid UUID: `" + input + "`");
                            }
                        })
                .build();
    }
}

