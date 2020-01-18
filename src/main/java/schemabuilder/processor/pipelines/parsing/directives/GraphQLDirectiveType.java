package schemabuilder.processor.pipelines.parsing.directives;

import graphql.schema.idl.SchemaDirectiveWiring;
import java.util.Objects;

public class GraphQLDirectiveType {

    private String name;
    private SchemaDirectiveWiring directive;

    public GraphQLDirectiveType(String name, SchemaDirectiveWiring directive) {
        this.name = name;
        this.directive = directive;
    }

    public String getName() {
        return name;
    }

    public SchemaDirectiveWiring getDirective() {
        return directive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphQLDirectiveType that = (GraphQLDirectiveType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "GraphQLDirectiveType{" +
                "name='" + name + '\'' +
                '}';
    }
}
