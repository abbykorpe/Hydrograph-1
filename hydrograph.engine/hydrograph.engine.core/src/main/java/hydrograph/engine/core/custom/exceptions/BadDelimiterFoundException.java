package hydrograph.engine.core.custom.exceptions;

public class BadDelimiterFoundException extends RuntimeException{

    public BadDelimiterFoundException(String message) {
        super(message);
    }

    public BadDelimiterFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
