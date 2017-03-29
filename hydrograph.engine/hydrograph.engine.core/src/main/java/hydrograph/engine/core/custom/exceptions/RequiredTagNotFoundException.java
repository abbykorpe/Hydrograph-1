package hydrograph.engine.core.custom.exceptions;

public class RequiredTagNotFoundException extends RuntimeException{

    public RequiredTagNotFoundException(String message) {
        super(message);
    }

    public RequiredTagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
