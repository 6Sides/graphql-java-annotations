package core.directives.auth;

public interface PolicyChecker<T> {

    /**
     * Checks if the entity has permission.
     * Returns any object if they dont, `null` if they do.
     * @param policy
     * @return
     */
    Object hasPermission(T ctx, String policy);

}
