package schemabuilder.annotations.graphql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@GraphQLSchemaConfiguration
public @interface GraphQLDirective {

    /**
     * The name of the GraphQL directive the class is representing.
     *
     * @return The name of the GraphQL directive to represent
     */
    String value();

}
