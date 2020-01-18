package schemabuilder.processor.pipelines.parsing.typeresolvers;

import graphql.schema.TypeResolver;
import java.util.Objects;

public class GraphQLTypeResolverType {

    private String name;
    private TypeResolver resolver;

    public GraphQLTypeResolverType(String name, TypeResolver resolver) {
        this.name = name;
        this.resolver = resolver;
    }

    public String getName() {
        return name;
    }

    public TypeResolver getResolver() {
        return resolver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphQLTypeResolverType that = (GraphQLTypeResolverType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "GraphQLTypeResolverType{" +
                "name='" + name + '\'' +
                '}';
    }
}
