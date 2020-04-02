package schemabuilder.annotations.documentation

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Denotes that the annotated type is exposed via the API and must remain
 * backwards-compatible between releases.
 */
@Documented
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CONSTRUCTOR)
@Retention(RetentionPolicy.SOURCE)
annotation class Stable