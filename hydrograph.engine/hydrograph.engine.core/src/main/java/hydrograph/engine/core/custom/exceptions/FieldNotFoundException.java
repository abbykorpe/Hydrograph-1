package hydrograph.engine.core.custom.exceptions;

public class FieldNotFoundException extends RuntimeException{

    public FieldNotFoundException(String message) {
        super(message);
    }

    public FieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
