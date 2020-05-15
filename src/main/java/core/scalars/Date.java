package core.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import schemabuilder.annotations.graphql.GraphQLScalar;
import schemabuilder.annotations.graphql.GraphQLSchemaConfiguration;

@GraphQLSchemaConfiguration
public class Date {

    @GraphQLScalar("Date")
    public static GraphQLScalarType getDateScalar() {
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("A custom scalar that handles date values")
                .coercing(
                        new Coercing<LocalDate, String>() {
                            @Override
                            public String serialize(Object dataFetcherResult)
                                    throws CoercingSerializeException {
                                if (!(dataFetcherResult instanceof LocalDate)) {
                                    throw new CoercingSerializeException(
                                            "Unable to serialize "
                                                    + dataFetcherResult
                                                    + " as a Date");
                                }

                                return dataFetcherResult.toString();
                            }

                            @Override
                            public LocalDate parseValue(Object input)
                                    throws CoercingParseValueException {
                                String possibleDate = (String) input;
                                try {
                                    return LocalDate.parse(possibleDate);
                                } catch (DateTimeParseException ex) {
                                    throw new CoercingParseValueException(ex.getMessage());
                                }
                            }

                            @Override
                            public LocalDate parseLiteral(Object input)
                                    throws CoercingParseLiteralException {
                                if (input instanceof StringValue) {
                                    String possibleDate = ((StringValue) input).getValue();

                                    try {
                                        return LocalDate.parse(possibleDate);
                                    } catch (DateTimeParseException ex) {
                                        throw new CoercingParseLiteralException(ex.getMessage());
                                    }
                                }
                                throw new CoercingParseLiteralException(
                                        "Value is not a valid Date: `" + input + "`");
                            }
                        })
                .build();
    }
}

