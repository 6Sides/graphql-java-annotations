package core.directives.auth;

public interface PolicyCheck {

    /**
     * Checks if the entity has permission.
     * Returns any object if they dont, `null` if they do.
     * @param policy
     * @return
     */
    Object hasPermission(String policy);

}
