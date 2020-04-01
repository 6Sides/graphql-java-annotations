package core.directives.auth;

/**
 * Provides an object to check a requests policy
 */
public interface PolicyCheckProvider<T> {

    PolicyChecker<T> create();

}
