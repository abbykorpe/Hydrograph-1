package hydrograph.engine.core.custom.exceptions;

public class LengthMisMatchException extends RuntimeException{

    public LengthMisMatchException(String message) {
        super(message);
    }

    public LengthMisMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
