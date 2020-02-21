package core.directives.auth;

public interface PermissionCheck {

    boolean hasPermission(String permission);

}
