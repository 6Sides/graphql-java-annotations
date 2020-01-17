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
public @interface GraphQLTypeResolver {

    /**
     * The name of the type to associate the type resolver with
     *
     * @return
     */
    String value();

}
