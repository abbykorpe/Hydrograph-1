package hydrograph.engine.core.custom.exceptions;

public class BadQuoteFoundException extends RuntimeException{

    public BadQuoteFoundException(String message) {
        super(message);
    }

    public BadQuoteFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
