package core.scalars;

import graphql.language.IntValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.math.BigInteger;
import java.time.Instant;
import schemabuilder.annotations.graphql.GraphQLScalar;
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration;

@GraphQLSchemaConfiguration
public class DateTimeScalar {

    @GraphQLScalar("DateTime")
    public static GraphQLScalarType getDateTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("A custom scalar that handles datetime values")
                .coercing(
                        new Coercing() {
                            @Override
                            public Object serialize(Object dataFetcherResult)
                                    throws CoercingSerializeException {
                                if (!(dataFetcherResult instanceof Number)) {
                                    throw new CoercingSerializeException(
                                            "Unable to serialize "
                                                    + dataFetcherResult
                                                    + " as a DateTime");
                                }
                                return Instant.ofEpochSecond((Long) dataFetcherResult).toString();
                            }

                            @Override
                            public Object parseValue(Object input)
                                    throws CoercingParseValueException {
                                Long possibleDateTime = (Long) input;
                                try {
                                    return Instant.ofEpochSecond(possibleDateTime);
                                } catch (IllegalArgumentException ex) {
                                    throw new CoercingParseValueException(ex.getMessage());
                                }
                            }

                            @Override
                            public Object parseLiteral(Object input)
                                    throws CoercingParseLiteralException {
                                if (input instanceof IntValue) {
                                    BigInteger possibleDateTime = ((IntValue) input).getValue();

                                    try {
                                        Instant result =
                                                Instant.ofEpochSecond(
                                                        possibleDateTime.longValueExact());
                                        return result;
                                    } catch (IllegalArgumentException ex) {
                                        throw new CoercingParseLiteralException(ex.getMessage());
                                    }
                                }
                                throw new CoercingParseLiteralException(
                                        "Value is not a valid DateTime: `" + input + "`");
                            }
                        })
                .build();
    }
}

