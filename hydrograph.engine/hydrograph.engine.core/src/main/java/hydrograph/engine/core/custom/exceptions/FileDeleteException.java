package hydrograph.engine.core.custom.exceptions;

public class FileDeleteException extends RuntimeException{

    public FileDeleteException(String message) {
        super(message);
    }

    public FileDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
