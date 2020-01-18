package schemabuilder.processor.pipelines.parsing.scalars;

import java.util.Objects;

public class GraphQLScalarType {

    private String name;
    private graphql.schema.GraphQLScalarType scalar;

    public GraphQLScalarType(String name, graphql.schema.GraphQLScalarType scalar) {
        this.name = name;
        this.scalar = scalar;
    }

    public String getName() {
        return name;
    }

    public graphql.schema.GraphQLScalarType getScalar() {
        return scalar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphQLScalarType that = (GraphQLScalarType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "GraphQLScalarType{" +
                "name='" + name + '\'' +
                '}';
    }
}
