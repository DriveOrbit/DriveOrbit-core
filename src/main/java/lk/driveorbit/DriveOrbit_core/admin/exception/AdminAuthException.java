package lk.driveorbit.DriveOrbit_core.admin.exception;

/**
 * Custom exception for admin authentication related errors
 */
public class AdminAuthException extends Exception {
    
    private final String errorCode;

    public AdminAuthException(String message) {
        super(message);
        this.errorCode = "ADMIN_AUTH_ERROR";
    }

    public AdminAuthException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AdminAuthException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ADMIN_AUTH_ERROR";
    }

    public AdminAuthException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

/**
 * Exception thrown when access is denied due to insufficient privileges
 */
class AdminAccessDeniedException extends AdminAuthException {
    
    public AdminAccessDeniedException(String message) {
        super(message, "ACCESS_DENIED");
    }
}

/**
 * Exception thrown when admin user is not found
 */
class AdminNotFoundException extends AdminAuthException {
    
    public AdminNotFoundException(String message) {
        super(message, "ADMIN_NOT_FOUND");
    }
}

/**
 * Exception thrown when attempting to create an admin with existing email
 */
class AdminAlreadyExistsException extends AdminAuthException {
    
    public AdminAlreadyExistsException(String message) {
        super(message, "ADMIN_ALREADY_EXISTS");
    }
}

/**
 * Exception thrown when Firebase token is invalid
 */
class InvalidTokenException extends AdminAuthException {
    
    public InvalidTokenException(String message) {
        super(message, "INVALID_TOKEN");
    }
    
    public InvalidTokenException(String message, Throwable cause) {
        super(message, "INVALID_TOKEN", cause);
    }
}
