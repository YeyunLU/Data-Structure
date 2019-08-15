package misc.exceptions;

public class EmptyContainerException extends RuntimeException {
    public EmptyContainerException() {
        super();
    }

    public EmptyContainerException(String message) {
        super(message);
    }

    public EmptyContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyContainerException(Throwable cause) {
        super(cause);
    }
}
