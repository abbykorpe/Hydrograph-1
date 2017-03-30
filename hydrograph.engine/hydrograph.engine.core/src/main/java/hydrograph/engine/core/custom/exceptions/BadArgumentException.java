package hydrograph.engine.core.custom.exceptions;

public class BadArgumentException extends RuntimeException{

    public BadArgumentException(String message) {
        super(message);
    }

    public BadArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
