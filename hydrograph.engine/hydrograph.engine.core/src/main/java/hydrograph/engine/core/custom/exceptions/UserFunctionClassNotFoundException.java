package hydrograph.engine.core.custom.exceptions;
/**
 * The Class UserFunctionClassNotFoundException.
 *
 * @author Bitwise
 *
 */
public class UserFunctionClassNotFoundException extends RuntimeException {

    public UserFunctionClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserFunctionClassNotFoundException(String message) {
        super(message);
    }
}
