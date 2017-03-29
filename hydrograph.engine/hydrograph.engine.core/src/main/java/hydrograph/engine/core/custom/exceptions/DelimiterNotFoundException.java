package hydrograph.engine.core.custom.exceptions;

public class DelimiterNotFoundException extends RuntimeException{

    public DelimiterNotFoundException(String message) {
        super(message);
    }

    public DelimiterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
