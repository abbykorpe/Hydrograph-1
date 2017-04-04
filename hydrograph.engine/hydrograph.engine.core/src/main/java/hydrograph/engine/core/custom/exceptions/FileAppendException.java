package hydrograph.engine.core.custom.exceptions;

public class FileAppendException extends RuntimeException{

    public FileAppendException(String message) {
        super(message);
    }

    public FileAppendException(String message, Throwable cause) {
        super(message, cause);
    }
}
