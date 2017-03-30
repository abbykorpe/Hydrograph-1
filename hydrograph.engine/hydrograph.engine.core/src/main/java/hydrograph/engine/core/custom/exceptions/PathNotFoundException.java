package hydrograph.engine.core.custom.exceptions;
/**
 * The Class UserFunctionClassNotFoundException.
 *
 * @author Bitwise
 *
 */
public class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathNotFoundException(String message) {
        super(message);
    }
}
