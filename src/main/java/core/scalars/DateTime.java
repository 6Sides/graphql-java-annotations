package core.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import schemabuilder.annotations.graphql.GraphQLScalar;
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration;

@GraphQLSchemaConfiguration
public class DateTime {

    // Format in ISO 8601
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

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
                                if (!(dataFetcherResult instanceof OffsetDateTime)) {
                                    throw new CoercingSerializeException(
                                            "Unable to serialize "
                                                    + dataFetcherResult
                                                    + " as a DateTime");
                                }

                                return formatter.format(dataFetcherResult);
                            }

                            @Override
                            public Object parseValue(Object input)
                                    throws CoercingParseValueException {
                                String possibleDateTime = (String) input;
                                try {
                                    return OffsetDateTime.parse(possibleDateTime);
                                } catch (DateTimeParseException ex) {
                                    throw new CoercingParseValueException(ex.getMessage());
                                }
                            }

                            @Override
                            public Object parseLiteral(Object input)
                                    throws CoercingParseLiteralException {
                                if (input instanceof StringValue) {
                                    String possibleDateTime = ((StringValue) input).getValue();

                                    try {
                                        return OffsetDateTime.parse(possibleDateTime);
                                    } catch (DateTimeParseException ex) {
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

