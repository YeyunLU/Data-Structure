package misc.exceptions;

public class NoPathExistsException extends RuntimeException {
    public NoPathExistsException() {
        super();
    }

    public NoPathExistsException(String message) {
        super(message);
    }

    public NoPathExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPathExistsException(Throwable cause) {
        super(cause);
    }
}
