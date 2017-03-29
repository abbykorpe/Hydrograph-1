package hydrograph.engine.core.custom.exceptions;

public class SchemaMismatchException extends RuntimeException {

    public SchemaMismatchException(String message) {
        super(message);
    }

    public SchemaMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
