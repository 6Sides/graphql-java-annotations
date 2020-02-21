package core.directives.auth;

public interface PermissionCheck {

    /**
     * Checks if the entity has permission.
     * Returns any object if they dont, `null` if they do.
     * @param permission
     * @return
     */
    Object hasPermission(String permission);

}
