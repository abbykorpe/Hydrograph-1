package hydrograph.engine.core.custom.exceptions;

public class DependantComponentNotFoundException extends RuntimeException{

    public DependantComponentNotFoundException(String message) {
        super(message);
    }

    public DependantComponentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
