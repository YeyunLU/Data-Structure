package misc.exceptions;

public class InvalidElementException extends RuntimeException {
    public InvalidElementException() {
        super();
    }

    public InvalidElementException(String message) {
        super(message);
    }

    public InvalidElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidElementException(Throwable cause) {
        super(cause);
    }
}
