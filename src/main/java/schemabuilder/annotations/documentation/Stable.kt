package schemabuilder.annotations.documentation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated type is exposed via the API and must remain
 * backwards-compatible between releases.
 */
@Documented
@Target({ TYPE, METHOD, CONSTRUCTOR })
@Retention(RetentionPolicy.SOURCE)
public @interface Stable {

}
