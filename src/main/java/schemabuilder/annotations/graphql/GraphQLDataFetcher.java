package schemabuilder.annotations.graphql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GraphQLDataFetcher {

    /**
     * The field name to be associated with the DataFetcher.
     * If none is provided the name of the method is used.
     *
     * @return
     */
    String value() default "";


    int cost() default 0;

}
