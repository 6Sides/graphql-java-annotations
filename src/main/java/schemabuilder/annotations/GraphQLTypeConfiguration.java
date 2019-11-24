package schemabuilder.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@GraphQLSchemaConfiguration
public @interface GraphQLTypeConfiguration {

    /**
     * The graphql type name to associate datafetchers with.
     * @return
     */
    String value();

}
