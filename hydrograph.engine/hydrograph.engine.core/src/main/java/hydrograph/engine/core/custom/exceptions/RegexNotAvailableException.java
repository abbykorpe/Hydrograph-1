package hydrograph.engine.core.custom.exceptions;

public class RegexNotAvailableException extends RuntimeException{
    public RegexNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
    public RegexNotAvailableException(String message) {
        super(message);
    }
}
