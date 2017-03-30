package hydrograph.engine.core.custom.exceptions;
/**
 * The Class UserFunctionClassNotFoundException.
 *
 * @author Bitwise
 *
 */
public class DateFormatException extends RuntimeException {

    public DateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateFormatException(String message) {
        super(message);
    }
}
