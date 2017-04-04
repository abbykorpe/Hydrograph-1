package hydrograph.engine.core.custom.exceptions;

public class FileAlreadyExistsException extends RuntimeException{

    public FileAlreadyExistsException(String message) {
        super(message);
    }

    public FileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
