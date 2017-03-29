package hydrograph.engine.core.custom.exceptions;

public class OperationNotFoundException extends RuntimeException{

    public OperationNotFoundException(String message) {
        super(message);
    }

    public OperationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
